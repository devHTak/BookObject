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
