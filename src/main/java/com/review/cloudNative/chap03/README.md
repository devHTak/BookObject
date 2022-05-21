#### chap03. 12요소에 대한 애플리케이션 설정

##### 애플리케이션 설정이란?
- 보통 빈을 어떻게 연결할 지 컨테이너에게 알려주는 역할을 하는 스프링의 다양한 애플리케이션 컨텍스트 구현체에 대한 입력값을 의미한다.
- ClassPathXmlApplicationContext: XML 설정 파일을 통한 설정
- AnnotationConfigApplicationContext: Annotation 기반에 java 방식 설정

- 12 요소 애플리케이션에서 정의한 설정은 실행환경에서 달라질 수 있는 값을 의미한다.
  - 예시) DB 접속 정보

##### Spring Framework 에서 설정

- PropertyPlaceholderConfigurer 클래스
  - 외부화된 properties 파일에 정의된 값을 읽어 들이는 설정
- Environment 추상화와 @Value
  - Environment 추상화는 실행되는 애플리케이션과 그 애플리케이션이 실행되는 환경 사이에 참조를 통한 런타임 간접지정(indirection) 제공
  - Environment에 있는 PropertySource를 설정하면 키와 값을 읽을 수 있는 위치를 지정할 수 있다.
    ```java
    @Configuration
    @PropertySource("some.properties")
    public class Application { 
        @Bean
        static PropertySourcesPlaceholderConfigurer pspc() {
            return new PropertySourcesPlaceholderConfigurer();
        }
        @Value("${configuration.projectName}") private String fieldName;
    }
    ``` 
- Environment에는 Profiles를 사용할 수 있다.
  - 프로파일을 사용하면 빈을 그룹지어 사용할 수 있으므로, 실행 환경에 따라 달라지는 빈과 빈 그래프를 프로파일 변경만으로 쉽게 교체 적용할 수 있다.
  - 한 번에 하나 이상의 프로파일을 활성화할 수 있다.
  - XML 파일이나 태그 클래스, 설정 클래스, 개별 빈, @Bean이 붙은 메서드에 @Profile 애노테이션을 사용해 프로파일을 지정할 수 있다.
  - spring.profiles.active 를 통해 프로파일을 활성화 할 수 있으며, SPRING_PROFILES_ACTIVE 라는 환경변수나 -Dspring.profiles.active=.. 값으로 JVM 옵션을 사용해 지정할 수 있다.

##### 스프링 부트에서 설정
- 스프링 부트는 별다른 설정 없이 지정된 위치에 정의된 설정 정보를 자동으로 읽어온다.
  - 우선순위 1. 명령행 인자로 지정된 값
  - 우선순위 2. JNDI로 읽어온 값(java:comp/env에 있는 JNDI 속성)
  - 우선순위 3. System.getProperties()로 읽어온 값
  - 우선순위 4. 운영체제의 환경 변수
  - 우선순위 5. jar 외부에 존재하는 applicatoin.properties(yml) 에 지정된 속성
  - 우선순위 6. jar 내부에 존재하는 applicatoin.properties(yml) 에 지정된 속성
  - 우선순위 7. @Configuration이 붙은 클래스에 @PropertySource 로 지정된 곳에 있는 속성
  - 우선순위 8. SpringApplication.getDefaultProperties()로 읽어올 수 있는 기본값
- 특정 프로파일이 활성화되어 있으면, 프로파일 이름을 기준으로 src/main/resources/application-profilesName.yml 파일에 있는 속성 정보를 자동으로 읽어온다.
  ```java
  @EnableConfigurationProperties
  @SpringBootApplication
  public class Application {

    private final Logger Log = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
      SpringApplication.run(CloludNativeJavaApplication.class, args);
    }

    @Autowired
    public CloludNativeJavaApplication(ConfigurationProjectProperties cp) {
      Log.info("ConfigurationProjectProperties projectName: " + cp.getProjectName());
    }
  }
  @Component
  @ConfigurationProperties("configurations")
  public class ConfigurationProjectProperties {
      private String projectName;

      public String getProjectName() {
          return projectName;
      }

      public void setProjectName(String projectName) {
          this.projectName = projectName;
      }
  }
  ```
  - @EnableConfigurationProperties 는 스프링에게 설정 정보를 @ConfigurationProperties가 붙은 POJO에 매핑하라고 알려준다.
  - configuration으로 시작하는 설정값은 @ConfigurationProperties("configurations") 에 붙은 빈에 매핑된다.
  - 필드인 projectName은 configuration.projectName으로 지정된 설정값에 할당된다.
  
- @ConfigurationProperties 메커니즘은 광범위하게 사용된다.
  - actuator 의존관계를 추가하고 http://127.0.0.1:8080/configprops에 접속하면 어떤 설정 정보가 사용되는 지 확인할 수 있다.

##### 스프링 클라우드 설정 서버로 중앙 집중형 설정 사용하기

- 현재까지의 문제점
  - 애플리케이션 설정 정보를 변경하기 위해서는 애플리케이션을 재시작해야 한다.
  - 추적성이 없다.
  - 설정이 분산되어 있어 파악하기 어렵다
  - 손쉬운 암/복호화 방법이 없다.

- 스프링 클라우드 설정 서버
  - 스프링 클라우드 설정 서버가 애플리케이션 재시작, 암/복호화 문제를 해결해준다.
  - 스프링 클라우드 설정 서버는 클라이언트와 설정 정보와 설정 정보 저장소로의 통신 과정에 보안 기능을 추가할 수 있다.

- 스프링 클라우드 설정 클라이언트
  - bootstrap.properties
    - application.properties와 공존하면 bootstrap.properties가 먼저 로딩된다.
  - 스프링 클라우드 설정 클라이언트에는 @RefreshScope 를 통해 재시작 없이 설정 변경 사항을 반영할 수 있다.
    ```java
    @RestController
    @RefreshScope
    public class ProjectController {
        private final String projectName;

        public ProjectController(@Value("${configuration.projectName}") String pn) { this.projectName = pn;}
    }
    ```
    - 리프레시 이벤트가 발생할 때마다 설정 정보를 새롭게 읽어와 다시 생성한다.
  - RefreshScopeRefreshedEvent는 Actuator 가 제공하는 refresh 종단점에 POST 요청을 보내도 된다.
  - 스프링 클라우드 버스를 사용하면 설정 변경 내용을 여러 설정 클라이언트에 한번에 적용할 수 있다.
    - rabbit mq, kafka, reactor project 등 다양한 메시징 기술을 통해 스프링 클라우드 스트림이 장착된 버스를 통해 연결
    - /bus/refresh 종단점 
