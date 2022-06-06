#### 메시징

- 다양한 형태의 메시징 애플리케이션
  - 이벤트 알림
  - 이벤트를 통한 상태 전송
  - 이벤트 소싱
- 메시지를 보내는 프로듀서와 컨슈머는 직접 연결하여 메시지를 주고 받는 것이 아닌 메시지 브로커에 접속

##### 스프링 인티그레이션을 사용한 이벤트 주도 아키텍처

- 연속적인 이벤트 주도 데이터 처리 방법
- 스프링 인티그레이션 프레임워크는 스프링 프레임워크의 MessageChannel 타입과 Message<T> 타입을 중심으로 만들어졌다
  - Message<T> 객체는 메시지 페이로드와 그에 대한 메타데이터를 제공하는 일련의 헤더 정보를 포함
  - MessageChannel 은 java.util.Queue 객체와 유사하며 Message<T> 객체는 MessageChannel 타입의 인스턴스를 통해 전달
- 스프링 인티그레이션은 다수, 이기종의 시스템간의 데이터 용동을 지원
- 흐름
  - 데이터는 Message<T> 인스턴스를 통해 평준화
  - 각 Message<T>는 페이로드와 헤더 정보를 포함하며, 이는 Map<K, V> 안의 페이로드에 대한 메타데이터의 형태로 서로 다른 메시징 컴포넌트간의 입력과 출력 지정
  - MessageChannel을 통해 메시지 컴포넌트 사이에 어떤 Message<T> 를 사용해 메시지를 주고 받을지 명시

- 메시지 종단점
  - MessageChannel 객체들은 메시지 종단점(Messaging Endpoints) 에 연결
  - 게이트웨이
    - 인바운드 게이트웨이: 외부 시스템으로부터 전송되는 요청을 받고 Message<T>로 처리하여 응답을 보낸다
    - 아웃바운드 게이트웨이: Message<T>를 받아 외부시스템으로 전달하고 전달한 시스템으로부터의 응답을 기다린다
  - 인바운드 어댑터: 외부로부터 받은 메시지를 스프릉 Message<T>로 변환
    - 폴링 어댑터(설정한 시간 간격 또는 데이터 량을 주기적으로 가져오는 역할) , 이벤트 주도 어댑터
  - 필터: 유입되는 메시지 처리에 적용해야 하는 조건의 묶음
  - 라우터: 유입되는 메시지를 받아 어느 다운스트림으로 전달해야 하는 지 모든 테스트를 적용하는 역할
  - 트랜스포머: 받은 메시지를 변경하거나, 추가적인 내용을 더하고 빼는 등의 작업을 수행한 후에 출력
  - 스플리터: 유입된 메시지를 몇가지 속성에 따라 여러 개의 더 작은 메시지로 나누어 다운스트림에 전달
  - 에그리케이터: 특정 속성에 관계가 있는 내용들로 모아 다운스트림에 전달

  - 간단한 컴포넌트에서 복잡한 시스템으로
    - 파일시스템을 10초 간격으로 폴링 후 Message<T>를 사용하여 다른 시스템에 새로운 파일 생성 이벤트 전달
    ```java
    @Configuration
    public class IntegrationConfiguration {
  
        private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
        @Bean
        IntegrationFlow etlFlow(@Value("${input-directory:${HOME}/Desktop/in") File dir) {
            // File adaptor를 설정 -> 어댑터 유입되는 메시지를 처리하는 방법과 지정한 디렉토리를 폴링 형식으로 설정
            return IntegrationFlows.from(Files.inboundAdapter(dir).autoCreateDirectory(true),
                    consumer -> consumer.poller(spec -> spec.fixedRate(1000)))
                    .handle(File.class, (file, headers) -> {
                        logger.info("we noticed a new file." + file);
                        return file;
                    }).routeToRecipients(spec -> { // 수신된 파일을 페이로드로 포워딩
                        spec.recipient(csv(), (Message m) -> hasExt(m.getPayload(), ".csv"))
                                .recipient(txt(), (Message m)-> hasExt(m.getPayload(), ".txt"));
                    }).get();
        }
        private boolean hasExt(Object f, String ext) {
            File file = File.class.cast(f);
            return file.getName().toLowerCase().endsWith(ext);
        }
        @Bean private MessageChannel csv() { return MessageChannels.direct().get(); }
        @Bean private MessageChannel txt() { return MessageChannels.direct().get(); }
        @Bean
        IntegrationFlow txtFlow() {
            return IntegrationFlows.from(txt()).handle(File.class, (file, header) -> {
                logger.info("file is text."); return null;
            }).get();
        }
        @Bean
        IntegrationFlow csvFlow() {
            return IntegrationFlows.from(csv()).handle(File.class, (file, header) -> {
                logger.info("file is csv."); return null;
            }).get();
        }
    }
    ```
    - 스프링 인터그레이션을 사용하여 새로운 java.io.File 에 생기는 이벤트가 발생할 때 구동하도록 할 수 있다.
  - 인바운드 파일 어댑터 제공
    - 인바운드 파일 어댑터 메시지가 유입되는 지 보고 있다가 새로운 메시지가 유입되면 JobLaunchRequest로 변형
    - JobLaunchRequest에는 어떤 작업(Job)이 실행되어야 할지 이 작업에 적용될 JobLaunchParameter가 포함된다
    - 이렇게 변환된 JobLaunchRequest는 JobLaunchingGateway로 전달되어 라우팅을 통해 적절한 JobExecution 객체를 통해 작업 실행
  - 하나의 주요 플로우에서 분기 실행을 위한 MessageChannel 생성
    ```java
    @Configuration
    public class BatchChannels {
        @Bean MessageChannel invalid() { return MessageChannels.direct().get(); }
        @Bean MessageChannel complete() { return MessageChannels.direct().get(); }
    }
    ```
    - 주 플로우인 eltFlow는 지렉토리를 고정된 주기로 모니터링하고 새로운 이벤트가 발생하면 JobLaunchRequest로 변환
      ```java
      @Configuration
      public class EltFlowConfiguration {
          @Bean
          IntegrationFlow etlFlow(@Value("${input-directory:${HOME}/Desktop/in}")File directory,
                            BatchChannels c, JobLauncher launcher, Job job) {
              return IntegrationFlows.from(Files.inboundAdapter(directory).autoCreateDirectory(true), cs -> {
                            cs.poller(p -> p.fixedRate(1000L));
              }).handle(File.class, (file, headers) -> {
                  String absolutePath = file.getAbsolutePath();
                  // JobParametersBuilder 를 통해 배치를 실행하기 위한 JobParameters 생성
                  JobParameters params = new JobParametersBuilder().addString("file", absolutePath).toJobParameters();
                  return MessageBuilder.withPayload(new JobLaunchRequest(job, params))
                        .setHeader(FileHeaders.ORIGINAL_FILE, absolutePath)
                        .copyHeadersIfAbsent(headers).build();
              }).handle(new JobLaunchingGateway(launcher)) // 설정된 작업과 변수를 JobLoauncingGateway 에 전달
              .routeToRecipients(spec -> spec.recipient(c.invalid(), this::notFinished)
                        .recipient(c.complete(), this::finished)).get();
          }
          private boolean notFinished(Message<?> msg) {
              Object payload = msg.getPayload();
              return JobExecution.class.cast(payload).getExitStatus().equals(ExitStatus.COMPLETED);
          }
          private boolean finished(Message<?> msg) {
              return !this.notFinished(msg);
          }
      }
      ```
    - completed 채널로 메시지가 유입되면 페이로드를 completed 디렉토리로 옮기고 기록된 데이터를 테이블에 질의한다
      ```java
      @Configuration
      public class FinishedFileFlowConfiguration {
            @Bean
            IntegrationFlow finishedJobsFlow(BatchChannels c, @Value("${input-directory:${HOME}/Desktop/completed}") File finished,
                                        JdbcTemplate jdbcTemplate) {
                    return IntegrationFlows.from(c.complete())
                        .handle(JobExecution.class, (je, headers) -> {
                            String ogFileName = String.class.cast(headers.get(FileHeaders.ORIGINAL_FILE));
                            File file = new File(ogFileName);
                            mv(file, finished);
                            List<Contact> contacts = jdbcTemplate.query("SELECT * FROM CONTACT",
                                  (rs, i) -> {
                                            return new Contact(
                                                      rs.getLong("id"), rs.getString("email"),
                                                      rs.getString("full_name"), rs.getBoolean("valid_email"));
                                  });
                            return null;
                    }).get();
            }
      }
      ```
      - 비슷하게 Invalid 채널에 메시지가 유입되면 페이로드를 errors 디렉토리로 옮기고 흐름을 종료할 수 있다.

##### 메시지 브로커, 브릿지, 경쟁적 컨슈머 패턴, 이벤트 소싱

- 위 예제처럼 모든 컴포넌트는 메시지를 생산, 소비하는 주체가 될 수 있다.
  - 하지만 파일시스템을 직접 구현하지는 않을 것이다
  - 대신 이벤트 소스(메시지 프로듀서)와 싱크(메시지 컨슈머)를 사용하여 비트랜잭션 메시지 처리 방법을 사용할 필요가 있다.
- 사가 패턴을 사용하면 모든 서비스에 발생하는 문제에 대한 보상트랜잭션을 설계할 수 있지만
  - 메시지 브로커를 사용하면 훨씬 간단하게 해결할 수 있다.

- 발행-구독
  - 발행-구독 방식에서는 유입된 하나의 메시지를 연결된 모든 구독자가 받는다.
  - 이벤트 소싱(도메인 내의 시스템에서 지속적으로 변경하는 상태에 대한 로그를 저장하기 위한 이벤트 처리 방법) 가능

- 점대점
  - 하나의 메시지를 단 하나의 컨슈머로 전달

##### 스프링 클라우드 스트림

- 스프링 클라우드 스트림은 스프링 인티그레이션 기반으로 서비스 간 연동 모델에 채널을 사용하는 것을 핵심으로 한다.
- 메시지 브로거를 사용하면 서비스 간 일반적인 연결 사례를 만들어 좀 더 간결한 메시징이 가능하다
  - DIRECT: 점대점
  - BROADCAST: 발행-구독 방식 
- 바인더
  - 브로커와 연결을 위해 사용
  - 레빗엠큐에 경우 spring-cloud-starter-stream-rabbit 사용

- 프로듀서
- 컨슈머