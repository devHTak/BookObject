#### 테스트

##### Spring Boot Application Test

- ApplicationTest는 간단한 통합 테스트 코드
  ```java
  @SpringBootTest
  class ApplicationTests {
      @Test
      void contextLoads() {}
  }
  ```
  - contextLoads 테스트의 목적은 애플리케이션 컨텍스트가 제대로 로딩되고 applicationContext에 주입되는지 확인 하는 것
  - @SpringBootTest는 스프링 부트 테스트 클래스임을 알려주며, 테스트 클래스가 애플리케이션 컨텍스트를 로딩할 수 있게 해주는 ContextConfiguration 탐색을 지원한다.
    - @SpringBootTest에 아무 파라미터도 지정하지 않으면 기본적으로 @SpringConfiguration 애노테이션에 의한 탐색 방식으로 ApplicationContext를 로딩
    - @SpringBootApplication에 포함된 저수준 애노테이션에는 @SpringConfiguration도 포함되어 있다.

##### 통합 테스트

- 통합테스트는 여러 컴포넌트의 연동을 통합하기 위해 스프링 컨텍스트가 필요하다.
- 단위테스트는 스프링 라이버리에 대한 의존 없이 개별 컴포넌트만을 테스트한다.

- 테스트 슬라이스
  - Spring Boot 1.4 이전: 스프링 컨텍스트 전체를 로딩한 후에 통합 테스트가 실행되도록 작성
    - 모든 통합 테스트에서 애플리케이션 컨텍스트 전체가 필요하지 않기 때문에 비효율
  - Spring Boot 1.4 이후: 애플리케이션을 구성하는 레이어의 일부만 선택적으로 자동 설정할 수 있는 테스트 슬라이스 개념이 도입
    - 특정 스타터 프로젝트를 다른 스타터 프로젝트로 깔끔하게 교체할 수 있다.

- 모의 기법 활용
  ```java
  public class ServiceTests {
      @MockBean private UserService userService;
      @MockBean private AccountRepository accountRepository;
      private AccountService accountService;
      
      @Before
      public void before() {
          this.accountService = new AccountService(userService, accountRepository);
      }
      
      @Test
      public void getUserAccountsReturnsSingleAccount() throws Exception {
          given(this.accountRepository.findAccountsByUsername("test"))
              .willReturn(Collections.singletonList(new Account("test")));
          given(this.userService.getAuthenticatedUser("test"))
              .willReturn(new User(0L, "test", "John", "Doe"));
              
          List<Account> actual = accountService.getUserAccounts();
          assertThat(actual).size().isEqualTo(1);
          // ...
      }
  }
  ```
  - 모의(mocking) 기법은 단위 테스트를 다루는 컨텍스트에서 통용되는 용어
  - 스프링 부트에서 모의 기법은 통합 테스트와 단위 테스트에서 의미가 다르지만, 두 가지 테스트 모두에서 사용된다는 점에서 다를 게 없다
    - 스프링 컨텍스트가 필요하냐, 전혀 필요 없냐에 차이
  - @MockBean을 통해 지정할 수 있다.
    - 마이크로서비스에서 HTTP로 원격 서비스와 통합해야 하는 많은 협력 컴포넌트에 의존하는 스프링 MVC 컨트롤러를 통합 테스트 하는 일이 매우 흔한데, 이때에도 사용할 ㅅ ㅜ있다.

- @SpringBootTest 서블릿 컨테이너 설정
  - @SpringBootTest 애노테이션은 스프링 부트 애플리케이션에서 애플리케이션 컨텍스트 전체를 사용하도록 설정된 통합 테스트할 때 사용
  - 텍스트 컨텍스트에 맞는 서블릿 환경을 지정할 수 있다.
    - webEnvironment 속성을 통해 애플리케이션이 런타임에 사용하는 내장 서블릿 컨테이너를 설정할 수 있다
      - MOCK: WebApplicationContext를 로딩하고 모의 서블릿 환경 제공
      - DEFINED_PORT: EmbeddedWebApplicationContext를 로딩하고 지정된 포트 번호를 통해 실제 서블릿 환경 제공
      - RANDOM_PORT: EmbeddedWebApplicationContext를 로딩하고 무작위 포트 번호를 통해 실제 서블릿 환경 제공
      - NONE: SpringApplication을 사용해 애플리케이션 컨텍스트를 로딩하지만 서블릿 환경을 제공하지 않는다.
    - 어떤 옵션을 선택하느냐에 따라 테스트 결과가 나오는 시간과 연관이 있다.
    
- 슬라이스
  - @JsonTest
    - JSON 직렬화/역직렬화 테스트를 위한 설정만을 활성화한다.
    ```java
    @Autowired private JacksonTester<User> json;
    private User user;
    
    @Beforee
    public void before() throws Exception {
        User user = new User("user", "test@test.com");
        user.setId(0L);
        this.user = user;
    }
    
    @Test
    public void deserializeJson() throws Exception {
        String content = "{\"username\": \"user\", \"email\": \"test@test.com\"}";
        assertThat(this.json.parse(content)).isEqualTo(new User("user", "test@test.com");
        assertThat(this.json.parseObject(content).getUsername()).isEqualTo("user");
    }
    
    @Test
    public void serializeJson() throws Exception {
        assertThat(this.json.write(user)).isEqualTo("user.json");
        // ...
        assertJsonPropertyEquals("@.username", "user");
        // ...
    }
    ```
    
  - @WebMvcTest
    - 스프링 부트 애플리케이션에서 개별 스프링 MVC 컨트롤러의 테스트를 지원하며 컨트롤러 메소드와의 상호작용하여 테스트할 수 있도록 Spring MVC Infrastructure 자동 설정
    - MockMvc 객체를 이용한 가짜 요청을 보내 테스트할 수 있다.
    
  - @DataJpaTest
    - 스프링 데이터 JPA를 사용하는 스프링 부트 애플리케이션 테스트를 편리하게 작성할 수 있고, 스프링 데이터 JPA 테스트에 사용될 내장 인메모리 데이터베이스도 제공
    ```java
    @Autowired private TestEntityManager entityManager;
    @Autowired private AccountRepository accountRepository;
    
    @Test
    public void findUserAccountShouldReturnAccounts() throws Exception {
        this.entityManager.persist(new Account("jack", ACCOUNT_NUMBER);
        List<Account> accounts = this.accountRepository.findAccountsByUsername("jack");
        assertThat(accounts).size().isEqualTo(1);
        // ...
    }
    ```
    
  - @RestClientTest
    - 스프링 RestTemplate을 사용해 REST 서비스와 통신하는 기능 지원
    - MockRestServiceServer를 이용하여 외부의 서비스의 동작을 원하는 대로 흉내내서 제어하고 고정할 수 있다.
    ```java
    @Autowired private MockRestServiceServer server;
    @Autowired private UserService userService;
    @Value("${user-service.host:user-service}") private String serviceHost;
    
    @Test
    public void getAuthenticatedUserShouldReturnUser() {
        this.server.expect(requestTo(String.format("http://%s/uaa/v1/me", serviceHost))
                .andRespond(withSuccess(new ClassPathResource("user.json", getClass()), MediaType.APPLICATION_JSON));
                
        User user = userService.getAuthenticatedUser();
        assertThat(user.getUsername()).isEqualTo("user");
    }
    ```
  
##### 전구간 테스트

- 전구간(end-to-end) 테스트는 분산 애플리케이션의 일부 컴포넌트를 변경하더라도 시스템이 동일한 결과를 반환하도록 보장해준다
- 비즈니스 시능 검증, 사용자의 관점에서 기능을 테스트

- 분산 시스템 테스트
  - 분산 시스템에 속하는 애플리케이션을 개발할 때에는 아키텍처 상에서 상태가 어떻게 공유되고 복제되는지 파악해야 할 필요가 있다.
  - 상태는 보통 데이터베이스 테이블에서 하나의 컬럼으로 표현되는 상태 필드를 의미
    - 일체형 애플리케이션에서는 상태가 여러 데이터베이스 인스턴스에 일관성을 유지하면서 복제될 수 있으며 이를 통해 고가용성을 확보할 수 있다.
    - 상태를 분산 저장하면서 일관성을 보장해야 하는 것은 어려운 문제이다.
  - 분리된 마이크로서비스에 상태를 분산 저장하는 클라우드 네이티브 애플리케이션을 테스트 할 때에는 데이터 일관성을 점검할 수 있는 전 구간 테스트 설계를 이해하는 것이 필수적이다.
    - 여러 마이크로서비스에 걸쳐 상태를 저장할 때 도달할 수 있는 가장 높은 수준의 일관성은 결과적 일관성(Eventual Consistency)이다.

- 컨슈머 주도 계약 테스트(Consumer-Driven Contract Testing)
  - 서비스 사이에 느슨한 결합을 유지하면서 공개된 계약을 사용하여 컨슈머와 프로듀서 사이의 예상 동작을 유지하고 판정하는 방식
  - MSA에 존재하는 프로듀서와 컨슈머가 스텁(stub)을 컨슈머 주도의 계약 형태로 공개하는 것을 허용
    - 계약에는 서비스 호출 인터페이스가 기술되며, 런타임에서 필요한 마이크로서비스 사이의 라이브러리는 공유하지 않는다.
    - 프로듀서가 먼저 계약과 그 계약을 사용하는 통합 테스트를 정의하면서 스텁을 공개한다
    - 컨슈머는 프로듀서가 공개한 스텁을 공유 저장소에서 내려받아 모의 프로듀서를 만든다.
    - 프로듀서는 계약 정의를 통햏 만들어진 스텁은 공유하지만 클래스나 클라이언트 라이브러리는 공유하지 않는다.

- 스프링 클라우드 컨트랙트
  - 컨슈머 주도 계약을 사용할 수 있는 프레임워크 컴포넌트 제공
  - 컨슈머 주도 계약은 마이크로서비스 사이에서 사용되는 REST API나 메시징 교환 같은 분산 애플리케이션 컴포너트를 통합 테스트할 때 공통적으로 사용
  - 예제
    - 사용자 서비스: Producer(계정 서비스에게 사용자 정보 조회 API 제공)
    - 계정 서비스: Consumer(사용자 서비스에서 사용자 정보 조회 API 사용)
    - 사용자 서비스
      - dependency
      ```
      implementation 'org.springframework.cloud:spring-cloud-contract-spec:2.2.8.RELEASE'
      ```
      - UserService
      ```java
      @Service
      public class UserService {

          private final UserRepository userRepository;

          @Autowired
          public UserService(UserRepository userRepository) {
              this.userRepository = userRepository;
          }

          public User getUserByPrincipal(Principal principal) {
              return Optional.ofNullable(principal)
                      .map(p -> userRepository.findByUsername(p.getName()))
                      .orElse(null);
          }
      }
      ```
      - UserController
      ```java
      @RestController
      @RequestMapping("/uaa/v1")
      public class UserController {

          private UserService userService;
          private AuthService authService;

          @Autowired
          public UserController(UserService userService, AuthService authService) {
              this.userService = userService; this.authService = authService;
          }

          @GetMapping("/me")
          public ResponseEntity<User> me(Principal principal) throws Exception {
              return Optional.ofNullable(authService.getAuthenticatedUser(principal))
                      .map(p -> ResponseEntity.ok().body(userService.getUserByPrincipal(p)))
                      .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
          }
      }
      ```
      - 컨슈머 주도계약 정의하는 테스트 소스 코드(shouldReturnUser.groovy)
      ```
      // 스프링 클라우드 컨트랙트로 만든 스텁은 컨슈머 주도 계약 테스트에서 사용되는 서비스의 동작을 흉내낸다.
      // 이런 동작은 사용자에게 공개된 REST API 메소드의 동작에 대해 서비스 프로듀서가 설정한 예상이므로 예상 동작이라고 부른다
      // ./src/main/test/resources/contracts/shouldReturnUser.groovy
      package contracts
      org.springframework.cloud.contract.spec.Contract.make {
          request{
              method 'GET'
              url '/uaa/v1/me'
              headers {
                  header('Content-Type': consumer(regex('application/*json*')))
              }
          }
          response {
              status 200
              body([
                      username: value(producer([regex('[A-Za-z0-9]+')]))
              ])
              headers {
                  header('Content-Type': value(
                          producer('application/json;charset=UTF-8'),
                          consumer('application/json;charset=UTF-8')
                  ))
              }
          }
      }
      ```
      - 빌드 설정
        - UserService 아티팩트를 로컬 저장소 또는 사설 아티팩트 저장소에 설치 필요
        - install 하 때마다 하나가 아닌 아티팩트 자신과 게약 정의를 포함하는 아티팩트 두 개씩 발행되도록 설정되어 있다.
      ```
      buildscript {
        repositories {
          maven {
            url "https://plugins.gradle.org/m2/"
          }
        }
        dependencies {
          classpath "gradle.plugin.org.springframework.cloud:spring-cloud-contract-gradle-plugin:3.1.2"
        }
      }

      apply plugin: "org.springframework.cloud.contract"
      ```
