#### chap11. 배치 처리와 태스크

##### 배치 작업

- 배치 처리란 일반적으로 입력된 값들을 일괄적으로 한꺼번에 처리하는 것을 의미
- 대량의 데이터를 처리할 때 매력적
  - sql, csv 파일같은 순차적 데이터를 배치 처리 작업에 적합

##### 스프링 배치

- 스프링 배치는 로깅 및 추적, 트랜잭션 관리, 작업처리 지표, 작업 재시작, 작업 무시, 자원 관리 포함
- Job -> Step -> ItemReader, ItemProcessor, ItemWriter
  - Job은 여러 Step을 갖는다.
  - 하나의 스텝에서 다음 스텝으로의 흐름은 기본 루프, 조건, 동시성을 바탕으로 라우팅 로직을 통해 진행
    - 스텝 안에서 비즈니스 기능을 tasklet을 사용하여 정의 가능하며 실행 순서를 조율 할 수 있다.
  - 각 단계를 ItemReader, ItemProcessor, ItemWriter 구현체를 통해 더 자세하게 프로세스 정의 가능

- 첫번째 배치 작업
  ```java
  @Configuration
  public class BatchConfiguration {

      @Bean
      Job estl(JobBuilderFactory jbf, StepBuilderFactory sbf,
             Step1Configuration sc1, Step2Configuration sc2, Step3Configuration sc3) throws Exception {
          // 형식이 자유로운 tasklet 콜백 사용
          Step setup = sbf.get("clean-contract-table").tasklet(null).build();

          Step s2 = sbf.get("file-db").<Person, Person>chunk(1000)
                .faultTolerant()
                // 재실행 정책
                .skip(InvalidEmailException.class).retry(HttpStatusCodeException.class) 
                .retryLimit(2).reader(sc2.fileReader(null))
                .processor(sc2.emailValidatingProcessor(null))
                .writer(sc2.jdbcWriter(null))
                .build();

          Step s3 = sbf.get("db-file")
                .<Map<Integer, Integer>, Map<Integer, Integer>>chunk(100)
                .reader(sc3.jdbcReader(null)).writer(sc3.fileWriter(null)).build();

          return jbf.get("etl").incrementer(new RunIdIncrementer())
                .start(setup)
                .next(s2).next(s3).build();
      }
  }
  ```
  - JobBuilderFactory와 StepBuilderFactory를 사용하여 Job 정의
  - 현재는 단일 스레드로 동작
    - Job을 위한 TaskExecutor 구현체를 사용하므로써 스프링 배치가 병렬적 실행 가능
  - 재실행 정책
    - HttpStatusCodeException이 발생하면 연결을 재시도하도록 설정
    - InvalidEmailException이 발생할 경우 건너뛰는 무시 정책 설정
  - Step 정의
    ```java
    // setUp단계로 전체 데이터 삭제 정의
    @Configuration
    public class Step1Configuration {
        @Bean
        Tasklet tasklet(JdbcTemplate jdbcTemplate) {
            return (contribution, chunkContext) -> {
                jdbcTemplate.update("delete from people");
                return RepeatStatus.FINISHED;
            };
        }
    }
    // File에서 읽어온 데이터 처리
    @Configuration
    public class Step2Configuration {
        // FlatFileItemReader를 통해 File 읽기 처리
        @Bean
        @StepScope
        FlatFileItemReader<Person> fileReader(@Value("file://#{jobParameters['input]}")Resource in) throws Exception {
            return new FlatFileItemReaderBuilder<Person>().name("file-reader")
                .resource(in).targetType(Person.class).delimited().delimiter(",")
                .names(new String[] {"firstName, age", "email"}).build();
        }
        // ItemProcessor는 차례로 REST API를 호출하는 EmailValidationService 위임 
        @Bean
        ItemProcessor<Person, Person> emailValidatingProcessor(EmailValidationService emailValidator){
            return item -> {
                String email = item.getEmail();
                if(!emailValidator.isEmailValid(email)) {
                    throw new InvalidEmailException(email);
                }
                return item;
            };
        }
        // ItemProcessor에 결과를 취하고 우리가 제공한 SQL query 문장으로 변환하여 저장
        @Bean
        JdbcBatchItemWriter<Person> jdbcWriter(DataSource dataSource) {
            return new JdbcBatchItemWriterBuilder<Person>()
                    .dataSource(dataSource)
                    .sql("insert into PERSON(EMAIL, FIRST_NAME, AGE) VALUES(:age, :firstName, :email)").beanMapped().build();
        }
    }
    @Configuration
    public class Step3Configuration {
        @Bean
        JdbcCursorItemReader<Map<Integer, Integer>> jdbcReader(DataSource dataSource) {
            return new JdbcCursorItemReaderBuilder<Map<Integer, Integer>>()
                  .dataSource(dataSource)
                  .name("jdbc-reader")
                  .sql("select COUNT(age) c, age a from PERSON group by age")
                  .rowMapper((rs, i) -> Collections.singletonMap(rs.getInt("a"), rs.getInt("c")))
                  .build();
        }

        @Bean
        @StepScope
        FlatFileItemWriter<Map<Integer, Integer>> fileWriter(@Value("file://#{jobParameters['input]}") Resource out) {
            DelimitedLineAggregator<Map<Integer, Integer>> aggregator = new DelimitedLineAggregator<Map<Integer, Integer>>();
            aggregator.setDelimiter(",");
            aggregator.setFieldExtractor(ageAndCount -> {
                Map.Entry<Integer, Integer> next = ageAndCount.entrySet().iterator().next();
                    return new Object[] { next.getKey(), next.getValue()};
            });

            return new FlatFileItemWriterBuilder<Map<Integer, Integer>>().name("file-writer")
                    .resource(out).lineAggregator(aggregator).build();
        }
    }
    ```
    - @StepScope 애노테이션은 싱글톤이 아닌, 작업 인스턴스가 실행될 때마다 새롭게 생성된다.
    - 배치 어플리케이션을 실행하기 위해서는 @EnableBatchProcessing 애노테이션을 실행해 주어야 한다.
    - Job은 기본적으로 동기 방식으로 실행
      ```java
      @Bean 
      CommandLineRunner run(JobLauncer launcher, Job job, @Value("${user.home}")String home) {
          return args -> launcher.run(job, new JobParameterBuilder().addString("input", path(home, "in.csv"))
                .addString("output", path(home, "out.csv")).toJobParameters());
      }
      ```
      - JobLauncher#run 메소드는 완료된 작업 상태 확인을 제공하는 JobExecution을 리턴한다.
      - JobLauncher 구성에 TaskExecutor가 있다면 작업을 비동기 방식으로 실행할 수 있다.
    - CommandLineRunner 나 ApplicationRunner 인터페이스의 구현체를 정의한 모든 스프링 부트 기반 서비스에서 활용 가능하다
      - 두 인터페이스 모두 스프링 빈을 통해 구현할 수 있는 콜백 인터페이스이며, 콜백 애플리케이션의 main 메서드에 선언한 args vkfkalxjfmf xhdgo wjsekf

##### 스케줄링

- 작업 스케줄에 정밀한 제어가 필요한 경우
  - ScheduleExecutorService 사용
  - 좀 더 추상화가 필요한 경우 java.util.concurrent.Executor 인스턴스를 위임한 스프링의 @Scheuled 애노테이션 사용
- 해당 방식의 단점은 클러스터에 대한 개념이 없다는 것
  - 작업 스케줄링 + 수명 주기에 더 많은 제어를 원한다면 쿼츠 엔터프라이즈 작업 스케줄러와 스프링 인티그레이션을 함께 사용
- 또 다른 접근 방법은 리더 선출 방식을 통해 클러스터에서 리더 노드를 선출하거나 제외하는 방법을 사용하는 것
  - 리더 노드는 작업 상태 정보를 유지할 필요가 없으며, 동일한 작업이 두 번 실행되는 등의 위험을 감수하는 역할을 한다

##### 메시징을 사용한 스프링 배치 작업의 원격 파티셔닝

- 스프링 배치는 병렬화를 두가지 방법으로 지원한다.
  - 원격 파티셔닝
  - 원격 청킹
  - 두 방법 모두 하나의 스텝에 대한 제어권을 다른 노드로 전달을 지원하는 동작 방식
  - 노드 간 전달에 필요한 연결은 일반적으로 메시징 사용
- 원격 파티셔닝
  - 레코드의 범위를 포함한 메시지를 다른 노드가 읽을 수 있도록 발생
  - 워커 노드가 리더 노드에 모든 권한을 갖고 실행하며 실행 결과를 리더 노드가 취합
  - 작업이 ItemReader, ItemWriter같이 IO 자원을 많이 요구한다면 원격 파티셔닝을 사용하는 것이 좋다.
- 원격 청킹
  - 프로세싱을 위한 데이터를 하나의 노드에서 읽고 이를 리더 노드에 전달하는 점이 다르다.
  - 수행중인 작업이 ItemProcessor와 같이 더 많은 CPU 자원을 필요로 한다면, 원격 청킹을 사용하는 것이 좋다.
- 대부분 배치 작업은 IO 집약적으로 원격 파티셔닝 구조를 많이 사용한다.

- 예시
  - 파티셔닝을 위한 프로파일
    ```java
    public class Profiles {
        public static final String WORKER_PROFILE = "worker";
        public static final String LEADER_PROFILE = "!" + WORKER_PROFILE;
    }
    ```
    - 리더 노드와 워커 노드는 MessageChannel을 통해 통신하며, 스프링 클라우드 스트림을 사용한 MessageChannel 인스턴스를 사용할 수 있다.
  - 애플리케이션 진입점
    ```java
    @EnableBatchProcessing
    @IntegrationComponentScan
    @SpringBootApplication
    public class SpringBatchApplication {
        public static void main(String[] args) {
            SpringApplication.run(SpringBatchApplication.class, args);
        }
        // 폴링에 필요한 MessageChannel 구현을 톹ㅇ해 기본 전역 폴러를 지정
        @Bean(name = PollerMetadata.DEFAULT_POLLER)
        PollerMetadata defaultPoller() {
            return Pollers.fixedRate(10, TimeUnit.SECONDS).get();
        }
        @Bean
        JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }
    ```
  - 파티셔닝 설정
    ```java
    @Configuration
    @Profile(Profiles.LEADER_PROFILE)
    public class JobConfiguration {
        @Bean
        Job job(JobBuilderFactory jbf,
                LeaderStepConfiguration lsc) {
            return jbf.get("job")
                    .incrementer(new RunIdIncrementer())
                    .start(lsc.stagingStep(null, null)) // stagingStep을 통해 NEW_PEOPLE 테이블을 리셋
                    .next(lsc.partitionStep(null, null, null, null)) // 파티셔닝 Step은 작업을 다른 노드에 분배
                    .build();
        }
    }
    ```
    - 두 개의 스텝을 처리하는 하나의 스프링 배치 Job
    - Tasklet을 사용하여 데이터베이스를 비우는 작업
  - 파티셔닝 Step 설정
    ```java
    Configuration
    class LeaderStepConfiguration {
        @Bean
        Step stagingStep(StepBuilderFactory sbf, JdbcTemplate jdbc) {
            return sbf.get("staging")
                .tasklet((contribution, chunkContext) -> {
                    jdbc.execute("truncate NEW_PEOPLE");
                    return RepeatStatus.FINISHED;
                }).build();
        }
        @Bean
        Step partitionStep(StepBuilderFactory sbf, Partitioner p, PartitionHandler ph, WorkerStepConfiguration wsc) {
            Step workerStep = wsc.workerStep(null);
            return sbf.get("partitionStep")
                    .partitioner(workerStep.getName(), p)
                    .partitionHandler(ph)
                    .build();
        }
        @Bean
        MessageChannelPartitionHandler partitionHandler(@Value("${partition.grid-size:4}") int gridSize, MessagingTemplate messagingTemplate,  JobExplorer jobExplorer) {
            MessageChannelPartitionHandler partitionHandler = new MessageChannelPartitionHandler();
            partitionHandler.setMessagingOperations(messagingTemplate);
            partitionHandler.setJobExplorer(jobExplorer);
            partitionHandler.setStepName("workerStep");
            partitionHandler.setGridSize(gridSize);
            return partitionHandler;
        }
        @Bean
        MessagingTemplate messagingTemplate(LeaderChannels channels) {
            return new MessagingTemplate(channels.leaderRequestsChannel());
        }
        @Bean
        Partitioner partitioner(JdbcOperations jdbcTemplate,
                                @Value("${partition.table:PEOPLE}") String table,
                                @Value("${partition.column:ID}") String column) {
            return new IdRangeParitioner(jdbcTemplate, table, column);
        }
    }
    ```
    - 프록시 노드처럼 동작하는 데, 워커 노드에 분배하는 역할
    - partitionStep에서는 원격 노드 중 어떤 노드가 워커 스텝을 Partitioner와 PartitionHandler로 호출할 지 정보를 알아야 한다.
    - PartitionHandler 는 리더 노드의 원본 StepExecution을 책임진다.
  - StepLocator 를 사용하여 스텝 실행 요청을 보내는 방법
    ```java
    @Configuration
    @Profile(Profiles.WORKER_PROFILE)
    public class WorkerConfiguration {
        @Bean
        StepLocator stepLocator() {
            return new BeanFactoryStepLocator();
        }
        @Bean
        StepExecutionRequestHandler stepExecutionRequestHandler(JobExplorer explorer, StepLocator stepLocator) {
            StepExecutionRequestHandler handler = new StepExecutionRequestHandler();
            handler.setStepLocator(stepLocator);
            handler.setJobExplorer(explorer);
            return handler;
        }
        @Bean
        IntegrationFlow stepExecutionRequestHandlerFlow(WorkerChannels channels, StepExecutionRequestHandler handler) {
            MessageChannel channel = channels.workerRequestsChannels();
            GenericHandler<StepExecutionRequest> h = (payload, headers) -> handler.handle(payload);
            return IntegrationFlows.from(channel)
                    .handle(StepExecutionRequest.class, h)
                    .channel(channels.workerRepliesChannels()).get();
        }
    }
    ```
    - 워커 노드는 실행할 작업을 브로커에서 선택하여 해당 요청을 StepExecutionRequestHandler로 보낸다.
    - 그러면 해당 핸들러는 StepLocator를 이용하여 실제로 워커 애플리케이션의 컨텍스트 내에서 실행할 작업을 확인하게 된다.
  - JDBC를 활용한 스텝 구현
    ```java
    @Configuration
    public class WorkerStepConfiguration {
        @Value("${partition.chunk-size}")
        private int chunk;
        @Bean
        @StepScope
        JdbcPagingItemReader<Person> reader(DataSource dataSource, @Value("#{stepExecutionContext['minValue']}") Long min,
                                            @Value("#{stepExecutionContext['maxValue]}") Long max) {
            MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
            queryProvider.setSelectClause("id as id, email as email, age as age, first_name as firstName");
            queryProvider.setFromClause("from PEOPLE");
            queryProvider.setWhereClause("where id >= " + min + "and id <= " + max);
            queryProvider.setSortKeys(Collections.singletonMap("id", Order.ASCENDING));

            JdbcPagingItemReader<Person> reader = new JdbcPagingItemReader<>();
            reader.setDataSource(dataSource);
            reader.setFetchSize(this.chunk);
            reader.setQueryProvider(queryProvider);
            reader.setRowMapper((rs, i) -> {
                return new Person(rs.getLong("id"), rs.getString("email"), rs.getString("firstName"), rs.getInt("age"));
            });
            return reader;
        }
        @Bean
        JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
            return new JdbcBatchItemWriterBuilder<Person>()
                    .beanMapped()
                    .dataSource(dataSource)
                    .sql("INSERT INTO NEW_PEOPLE(age, firstName, email) VALUES(:age, :firstName, :email)")
                    .build();
        }
        @Bean
        Step workerStep(StepBuilderFactory sbf) {
            return sbf.get("workerStep").<Person, Person>chunk(this.chunk)
                    .reader(reader(null, null, null)).writer(writer(null)).build();
        }
    }
    ```
  - 애플리케이션 컴포넌트들은 MessageChannel 구현체를 통해 서로 연결된다
    - leaderRequests, workerRequests, workerReplies 등 3개의 스트림