#### 데이터 관리

##### 데이터 모델링

- 도메인 주도 설계
- 관계형 데이터베이스 관리 시스템 (RDBMS)
  - 트랜잭션이 필요한 데이터 저장에 있어 필수 도구
- NoSQL
  - 특정 사용 사례의 요구를 해결하기 위해 최적화된 다양한 데이터 모델을 제공
  - NoSQL 데이터베이스 사용에 적합한 특정 데이터 모델의 경계 지어진 컨텍스트의 분리가 필요한 MSA 구현에 장점으로 적용할 수 있다.

##### 스프링 데이터

- 스프링 데이터 애플리케이션의 구조
  - 도메인 클래스
    - 도메인 데이터의 모델을 함수로 표현한 기본 클래스
    - private field와 public getter, setter를 제공
  - Repository
    - Repository(스프링 데이터 공통): 스프링 데이터 레포지토리의 핵심 추상화 제공
    - CrudRepository(스프링 데이터 공통): Repository에 기본 CRUD 제어를 추가한 확장
    - PagingAndSortingRepository(스프링 데이터 공통): 레코드에 대한 페이징과 정렬 도구를 추가한 CrudRepository 확장
    - JpaRepository(스프링 데이터 JPA): PagingAndSortingRepository에 JPA와 RDBMS DB 모델 지원 도구 추가
    - MongoRepository(스프링 데이터 몽고디비): PagingAndSortingRepository에 몽고디비와 다큐먼트 관리 도구 추가

##### 스프링 데이터 JPA

- 설정
    ```yaml
    spring:
      profiles:
        active: development
    --- 
    spring:
      profiles: development
      jpa:
        show_sql: false
        database: MYSQL
        generated-ddl: true
      datasource:
        url: jdbc:mysql://localhost:3306/dev
        username: root
        password: dbpass
    ---
    spring:
      profiles: test
      jpa:
        show_sql: false
        database: H2
      datasource:
        url: jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE
    ```
  - 개발과 테스트를 위한 두 개의 분리된 설정 프로파일로 준비
    - 이제 통합 테스트를 test 프로파일 설정 적용
    - 기본 프로파일인 development 프로파일이 통합테스트에 적용되길 원하지 않는다
  - 이런 경우, 통합테스트에 @ActiveProfiles를 명시해 주면 해결 가능

- 계정 컨텍스트 도메인 명시
  - 예제: com.review.chap09.jpa.entity
    - 계정
    - 고객
    - 신용카드
    - 주소
  - @Id @GeneratedValue(strategy = GenerationType.AUTO)
    - 기본키 자동 증가
  - @OneToMany
    - JPA Entity에 대한 FK(외래키) 관계 기술
    - cascade = CascadeType.ALL
      - ALL은 트랜잭션의 커밋이 참조된 모든 JPA Entity에 연속적으로 적용됨
    - fetch = FetchType.EAGER
      - EAGGER는 관계가 즉시 자동으로 채워짐을 나타낸다
  - @EntityListeners(AuditingEntityListener.class)
    - JPA 엔티티가 상속하는 슈퍼클래스를 지정하는 애노테이션
    - JPA 작업의 수명주기를 관찰하는 감사 콜백 리스너를 지정
  - @CreatedDate
    - 레코드의 생성할 때의 타임스탬프를 저장하는 애노데이션
  - @LastModifiedDate
    - 레코드에 수정이 발생했을 때의 타임스탬프를 업데이터하는 애노테이션

- 레포지토리
  - 예제: com.review.chap09.jpa.repository

- 통합테스트
  ```java
  @SpringBootTest(classes = AccountApplication.class)
  @ActiveProfiles(profiles = "test")
  public class AccountApplicationTest {
    @Autowired private CustomerRepository customerRepository;
  
    @Test 
    void customerTest() {
        Account account = new Account("12345");
        Customer customer = new Customer("Jane", "Doe", "jane.doe@gmail.com", account);
        
        Address address = new Address("test", null, "DC", "washington", "USA", AddressType.SHIPPING, 20500);
        customer.getAccount().getAddress().add(address);
  
        Customer returnCustomer = customerRepository.save(customer);
        Assert.assertNotNull(returnCustomer.getAccount());
        Assert.assertNotNull(returnCustomer.getCreatedAt());
        Assert.assertNotNull(returnCustomer.getLastModified());
  
        Assert.assertTrue(returnCustomer.getAccount().getAddress().stream()
            .anyMatch(add -> add.getStreet1().equals("test")));
        
        customerRepository.findByEmailContaining(customer.getEmail())
            .orElseThrow(() -> new AssertionFailedException(new RuntimeException(
                "there's a supposed to be a matching record.")));
    }
  }
  ```

##### 스프링 데이터 몽고디비

- 도큐먼트 중심의 데이터베이스
- 데이터가 JSON 형태로 계층화된 데이터로 저장된 것을 의미
- 특정 몽고디비 인스턴스 접근 설정
  - spring.data.mongodb.host
  - spring.data.mongodb.port
  - spring.data.mongodb.database

- 주문 서비스 컨텍스트 예시
  - 도메인
    - 예시: com.review.chap09.mongo.doc
    - 주문서
    - 주문
    - 제품
    - 배송
    - @Document: 몽고디비 내의 도큐먼트를 표현
    - @Id: 몽고디비 ID 지정 (ObjectId type)
    - 일대다 등에 표현을 객체로 지정 가능

  - 감사
    - 몽고디비 엔티티의 감사를 지원
      - 공통 필드를 위하여 BaseEntity 지정
      - com.review.chap09.mongo.doc.BaseEntity
    - EventListener
      - AbstractMongoEventListener 를 상속, 메소드를 오버라이드하여 필요한 시점에 Event Listener를 지정할 수 있다.
      - com.review.chap09.mongo.config.BaseSaveListener
  
##### 스프링 데이터 레디스

- 레디스 키-값 저장소의 스프링 연동 제공
  - 가장 유명한 키-값 저장을 제공하는 오픈소스 NoSQL이다
  - 인메모리 데이터 구조 스토어를 가진 도구로 유명하다
- 레디스가 지원하는 데이터 구조
  - String
  - List
  - Set
  - Hashes
  - Sorted set
  - Bitmap and HyperLogLogs

- 분산 데이터 스토어를 사용하는 장점은 여러 개의 프로세스와 애플리케이션과 서버가 동일한 키를 가진 값에 일관성을 가지고 작업할 수 있기 때문
  - 분산 데이터 스토어: 데이터 구조에 대한 작업이 가능한 디자인을 가진 저장소
  - 일반적으로 이런 작업을 다수의 애플리케이션이 함께 수행하기 위해서는 값에 어떤 작업을 수행하기 전에 언어에서 지원하는 데이터 구조로 역직렬화 하는 과정이 필요하다
- 레디스에 저장된 데이터 접근에 직렬화와 역직렬화 과정을 배제함으로써 많은 트랜잭션을 처리할 수 있다.
  - 프로그래밍 방식의 액세스를 통해 데이터 구조에 대한 작업을 API로 제공함으로서 해결
- 프로세스간 통신이나 메시징에 주로 사용

- Redis 전략
  - Look Aside
    - 캐시를 옆에 두고 필요할 때만 데이터를 캐시에 로드하는 전략이다. (key-value 형태로 저장됨)
      - 데이터 가져오는 요청
      - Redis에 먼저 요청
        - cache hit: 반환
        - cache miss: 데이터베이스에 데이터 요청, 해당 데이터를 레디스에 저장
    - 장점
      - 구조는 실제로 사용되는 데이터만 캐시에 저장되고, 레디스의 장애가 어플리케이션에 치명적인 영향을 주지 않는다
    - 단점
      - 캐시에 없는 데이터인 경우 더 오랜 시간이 걸림 
      - 캐시가 최신 데이터를 가지고 있는가? (데이터베이스에 데이터가 업데이트 된다고 레디스가 업데이트 되는건 아님)
  - Write-Throough
    - 데이터를 데이터베이스에 작성할 때마다 캐시에 데이터를 추가하거나 업데이트 
    - 이로 인해 캐시의 데이터는 항상 최신 상태로 유지할 수 있지만 쓰지 않는 데이터도 캐시에 저장되기 때문에 리소스 낭비 
      - 쓰기 지연 시간 증가
    - TTL(Time-to-live)을 꼭 사용하여 사용되지 않는 데이터를 삭제해야 한다.

  - 캐싱 예제
    - 스프링 데이터 레디스는 스프링 프레임워크의 CacheManager 추상화 구현을 사용
      - 마이크로서비스 구조에서 중앙화된 데이터 캐싱을 위한 선택
    - 설정
      - spring.redis.host, spring.redis.port 설정으로 특정 레디스 호스트에 연결
      - @EnableCaching 추가하여 사용
        - 스프링 부트는 스프링 캐시 CacheManager를 자동 설정
    - 서비스
      ```java
      @Service
      public class UserService {
          private final UserRepository userRepository;
          @Autowired
          public UserService(UserRepository userRepository) {
              this.userRepository = userRepository;
          }
    
          @Cacheable(value="user")
          public User getUser(String id) {
              return userRepository.findById(id);
          }
    
          @CachePut(value="user", key="#id")
          public User updateUser(String id, User user) {
              User result = null;
              if(!userRepository.exists(user.getId())) {
                  result = userRepository.findById(user.getId()); 
              }
              return result;
          }
    
          @CacheEvict(value="user", key="#id")
          public boolean deleteUser(String id) {
              boolean deleted = false;
              if(userRepository.findById(id)){
                  userRepository.delete(id);
                  deleted = true;
              }
              return deleted;
          }
      }
      ```
      - @Cacheable 
        - cache를 가져오는 메서드에 붙이는 애노테이션 
        - cacheNames는 cache 저장소 이름(ConcurrentMapCache), value도 같은 역할을 한다. 
        - key에 경우 캐시 데이터가 들어있는 key 값이며, ConcurrentHashMap의 key이다 
        - key에 대한 value가 존재하면 기존 value가 리턴되고, getCacheData 는 수행되지 않는다. 
      - @CachePut
        - cache 저장소(example.store1) 의 key에 대한 value 를 업데이트 할 때 사용 
      - @CacheEvict 
        - cache 저장소(example.store1) 의 key에 대한 value 를 삭제 할 때 사용
    - key
      - \#key: SpEL(Spring Expression Language) 문법을 사용 
      - String, Integer, Long 등의 값은 #변수명 형태로 사용 가능 
      - 객체안의 멤버변수를 비교하는 경우 #객체명.멤버명 형태로 사용 
      - 파라미터를 보고 KeyGenerator에 의해 default key를 생성 
        - 파라미터가 없는 경우: 빈값 
        - 하나인 경우: 해당 값 
        - 여러 개인 경우: 모든 파라미터를 포함한 키 생성(모든 파라미터 및 해시코드 조합)

    - 조건 부여
      ```java
      @CachePut(cacheNames="example.store1", key="#cacheData.value", condition="#cacheData.value.length() > 5")
      public CacheData updateCacheData(CacheData cacheData) { return cacheData; }  
      ```
      - cacheData에 대한 조건을 넣을 수 있다.