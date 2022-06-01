#### REST API

- REST는 웹상의 수많은 API를 지원하는 독보적인 인기의 프로토콜

##### 레너드 리차드슨의 성숙도 모델

- REST는 GET, PUT, POST, DELETE 등과 같은 동사와 HTTP Header, Status Code 등 HTTP가 이미 서비스 간 통신을 위해 제공하는 기능을 활용하여 비즈니스 상태 변경을 외부에 노출하는 것과 관련된 모든 것을 말한다.
- REST 성숙도 모델을 통한 원칙 준수 등급
  - LEVEL 0: POX의 늪
    - HTTP를 사용한 API 기술이지만, 그 이상 REST와 관련된 것은 없다.
  - LEVEl 1: 자원
    - 관련된 명사를 구별하기 위해 여러 개의 URI를 사용하는 API
  - LEVEl 2: HTTP 동사
    - 동사나 상태코드 같은 HTTP의 전송 기능으로서의 특징을 이용하는 API 기술 
  - LEVEl 3: HATEOAS 
    - 서비스가 제공하는 자원에 접근하기 위해 아무런 사전지식도 요구하지 않는 수준의 API 기술
    - 서비스의 구조를 알아내는 데 필요한 균일한 인터페이스를 통해 지속성을 가질 수 있다.

##### 스프링 MVC를 이용한 간단한 REST API

- 예제
  - com.review.cloudNative.chap06.CustomerRestController

##### 컨텐트 협상

- 핸들러 메소드는 객체 자체를 반환하기도 하고, ResponseEntity에 객체를 담아 반환하기도 한다
- 스프링 MVC가 반환값을 HttpMessageConverter라는 전략 객체를 이용하여 클라이언트가 원하는 방식으로 전환하여 반환
  - Accept 헤더를 통해 클라이언트가 지정한 미디어 타입을 파악
  - 반환할 데이터를 HttpMessageConverter를 통해 해당 미디어 타입에 맞는 형식으로 변환하여 반환
- 컨텐트 협상을 통하여 서로 다른 컨텐틑 타입을 요구하는 클라이언트의 요청을 하나의 동일한 서비스로 처리할 수 있다.

- 바이너리 데이터 읽고 쓰기
  - 예제
    - com.review.cloudNative.chap06.CustomerRestController
    - 스프링 MVC는 반환한 Resource에 있는 버퍼로부터 자동으로 파일, URI, InputStream을 반환
    - Callable을 반환하는 핸들러 메소드를 사용하면 서블릿 컨테이너의 스레드풀에서 얻어온 스레드가 아닌 Executor로 설정한 별도의 스레드풀에서 얻어온 스레드를 사용하여 요청 처리

##### 에러처리

- 스프링 MVC는 @ExceptionHandler를 통해 예외를 처리할 수 있다.
- @ControllerAdvice/@RestControllerAdvice를 통해 여러 컨트롤러에서 발생하는 예외를 한곳에서 처리할 수 있다.
- 예제
  - com.review.cloudNative.chap06.CustomerControllerAdvice
  - 새로운 미디어 타입을 지정할 수 있다.
  - @ExceptionHandler에서 정의한 예외가 발생했을 때 처리
  - error() 메서드를 통해 HttpHeader, status code를 지정하여 반환

##### API 버저닝

- 시멘틱 버저닝
  - 시맨틱 버전은 '메이저, 마이너, 패치'의 형식
  - 시멘틱 버저닝은 클라이언트가 어떤 API를 사용할 수 있는지를 알게 해준다.
  - 현실적으로 클라이언트는 새버전의 API에 맞출 준비가 되어 있지 않을 때가 많다.
- REST API에는 여러 방법이 마련되어 있다.
  - 버전을 URL에 인코딩 하는 방법
    ```java
    @GetMapping("/{version}/hello")
    public RespnoseEntity<Greeting> helloV2(@PathVariable String version) {}
    ```
  - HTTP 헤더 안에 인코딩하는 방법
    ```java
    private enum ApiVersion {
        v1, v2
    }
    @GetMapping(value="/hi", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> helloV2(@RequestHeader("X-API-Version") ApiVersion apiVersion) {}
    ```
  - ACCEPT 헤더에 명시된 컨텐트 타입의 일부로 인코딩 할 수 있다.
    ```java
    public static final String V1_MEDIA_TYPE_VALUE = "application/vnd.bootful.demo-v1+json";
    public static final String V2_MEDIA_TYPE_VALUE = "application/vnd.bootful.demo-v2+json";
    
    @GetMapping(value="/hi", produces=V1_MEDIA_TYPE_VALUE)
    public ResponseEntity<Greeting> helloV1() {}
    ```

##### REST API 문서화

- 참고: https://github.com/devHTak/devhtak.github.io/blob/master/_posts/spring/2022-05-10_Spring_Doc.md
