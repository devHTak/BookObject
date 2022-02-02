#### 캡슐화

##### 레코드 캡슐화하기

- 배경
  - 레코드는 연관된 여러 데이터를 직관적인 방식으로 묶을 수 있기 때문에 의미있는 단위로 전달할 수 있게 해준다
    - 가변 데이터를 저장할 때 레코드보다 객체를 사용하면 어떻게 저장했는지를 숨긴 채 각각의 메서드로 제공할 수 있다.
    - 불변 데이터에 경우 레코드에 저장

- 절차
  - 레코드를 담은 변수를 캡슐화한다
    - 레코드를 캡슐화하는 함수의 이름은 검색하기 쉽게 지어준다
  - 레코드를 감싼 단순 클래스로 해당 변수의 내용을 교체
    - 이 클래스에 원본 레코드를 반환하는 접근자도 정의하고, 변수를 캡슐화하는 함수들이 이 접근자를 사용하도록 수정
  - 테스트
  - 원본 레코드 대신 새로 정의한 클래스 타입의 객체를 반환하는 함수들을 새로 만든다
  - 레코드를 반환하는 예전 함수를 사용하는 코드를 새 함수를 사용하도록 바꾼다
    - 필드에 접근할 때는 객체의 접근자를 사용
    - 적절한 접근자가 없다면 추가, 한부분을 바꿀때마다 테스트
  - 클래스에서 원본 데이터를 반환하는 접근자와 첫번째 원본 레코드를 반환하는 함수 제거
  - 테스트
  - 레코드의 필드도 데이터 구조인 중첩 구조라면 레코드 캡슐화하기와 컬렉션 캡슐화하기를 재귀적으로 적용

##### 컬렉션 캡슐화하기

```java
class Person {
    private List<Course> courses;
    
    public List<Course> getCourses() { return this.courses; }
    public void setCourses(List<Course> courses) { this.courses = courses;}
  
    // getter, setter 대신 편의메소드 제공
    public void addCourse(Course course) { this.corses.add(course); }
    public void removeCourse(Course course) { 
        if(this.courses.contains(course)) {
            throw new IllegalArgumentException();
        }
        this.courses.remove(course);
    }
}
```

- 배경
  - 컬렉션을 캡슐화할 때 종종 실수를 저지르곤 한다
    - 컬렉션 변수로의 접근을 캡슐화하면서 getter가 컬레션 자체를 반환하도록 한다면 그 컬렉션은 감싼 클래스가 눈치채지 못하는 상태에서 컬렉션의 원소들이 바뀔 수 있다
    - add, remove 컬렉션 변경자 메서드를 만든다
  - 내부 컬렉션을 직접 수정하지 못하게 막는 방법
    - getter를 통하지 않고 처리할 수 있도록 편의 메소드 제공
      - customer.getOrders().size() 대신 customer.getNumberOfOrders()
    - 읽기 전용으로 제공
      - 읽기 전용 프락시 반환하여 연산은 그대로 전달하고 쓰기는 모두 막는다

- 절차
  - 아직 컬랙션을 캡슐화하지 않았다면 변수 캡슐화하기부터 한다
  - 컬렉션에 원소를 추가/제거하는 함수를 추가
    - 컬렉션 자체를 통째로 바꾸는 세터 제거
    - 세터를 제거할 수 없다면 인수로 받은 컬렉션을 복제해 저장
  - 정적 검사 수행
  - 컬렉션을 참조하는 부분을 모두 찾는다
    - 컬렉션의 변경자를 호출하는 코드가 모두 앞에서 추가한 추가/제거 함수를 호출하도록 수정
    - 하나씩 수정할 때마다 테스트
  - 컬렉션 게터를 수정하여 원본 내용을 수정할 수 없는 읽기 전용 프락시나 복제본을 반환
  - 테스트
  
##### 기본형을 객체로 바꾸기

```java
orders.filter(o -> o.getPriority().equals("high") || o.getPriority().equals("rush"))...

orders.filter(o -> o.getPriority.higerThan(new Priority("normal")))...;
```

- 배경
  - 단순한 정보를 숫자, 문자열같은 간단한 데이터 항목으로 표현할 때가 많다
    - 시간이 지날 수록 문자열 포매팅, 데이터 추출 등 특별한 동작이 추가되는 데, 이는 중복코드 등이 발생할 수 있다
  - 해당 데이터에 단순 이상의 기능이 필요해지면 전용 클래스를 정의하고, 특별한 동작이 추가되는 경우 메서드로 정의

- 절차
  - 아직 변수를 캡슐화하지 않았다면 캡슐화한다
  - 단순한 값 클래스를 만든다
    - 생성자는 기존 값을 인수로 받아 저장하고, 이 값을 반환하는 게터를 추가
  - 정적 검사 수행
  - 값 클래스의 인스턴스를 새로 만들어 필드에 저장하도록 세터 수정
    - 이미 있다면 필드의 타입을 적절히 변경
  - 새로 만든 클래스의 게터를 호출한 결과를 반환하도록 게터 수정
  - 테스트
  - 함수이름을 바꾸면 원본 접근자의 동작을 더 잘드러낼 수 있는지 검토

##### 임시 변수를 질의 함수로 바꾸기
```java
int basePrice = this.quantity * this.itemPrice;
if(baseePrice > 1000) {
    return basePrice * 0.95;
} else {
    return basePrice * 0.9;
}
```
```java
private int basePrice;
...
if(this.basePrice > 1000) {
    return this.basePrice * 0.95;
} else {
    return this.basePrice * 0.9;
}
```

- 배경
  - 함수 안에서 어떤 코드의 결과값을 뒤에서 다시 참조할 목적으로 임시 변수를쓰기도 한다
    - 임시변수는 동일한 코드 반복을 줄이고 값의 의미를 설명할 수 있어 유용하다
    - 한걸은 더 나아가 함수로 만들어 사용하는 편이 나을 때가 많다
  - 긴 함수의 한 부분을 함수로 추출할 때 추출한 변수를 따로 전달할 필요가 없어진다
    - 또한 경계가 더 분명해지며 이는 부자연스러운 의존관계나 부수효과를 찾고 제거하는 데 도움이 된다
  - 해당 리팩터링은 클래스 안에서 적용할 때 효과가 크다
    - 추출할 메서드들에 공유 컨텍스트를 제공하기 때문이다
    - 클래스 밖에서 최상위 함수로 추출하면 매개변수가 많아져 장점이 사라진다.

- 절차
  - 변수가 사용되기 전에 값이확실히 결정되는 지, 변수를 사용할 때마다 계산 로직이 다른 결과를 내지는 않는지 확인
  - 읽기전용으로 만들 수 있는 변수는 읽기 전용으로 만든다
  - 테스트
  - 변수 대입문을 함수로 추출
  - 테스트
  - 변수인라인하기로 임시 변수 제거

##### 클래스 추출하기

```java
class Person {
    private String officeAreaCode;
    private String officeNumber;
    
    public String getOfficeAreaCode() { return this.officeAreaCode; }
    public String getOfficeNumber() { return this.officeNumber; }
}
```
```java
class Person {
    private TelephoneNumber telephoneNumber;
    
    public String getOfficeAreaCode() { return this.telephoneNumber.getAreaCode(); }
    public String getOfficeNumber() { return this.telephoneNumber.getNumber(); }
}

class TelephoneNumber {
    private String areaCode;
    private String number;

    public String getAreaCode() { return this.areaCode; }
    public String getNumber() { return this.number; }
}
```

- 배경
  - 메서드와 데이터가 너무 많은 클래스는 이해하기 쉽지 않으니 잘 살펴보고 적절히 분리하는 것이 좋다
    - 일부 데이터와 메서드를 따로 묶을 수 있으면 분리하라는 신호이며 함께 변경되는 일이 많거나 서로 의존하는 데이터도 분리
  - 작은 일부늬 기능만을 위해 서브클래스를 만들거나 확장해야할 기능이 무엇이냐에 따라 서브클래스를 만드는 방식도 달라진다면 클래스를 나눠야 한다

- 절차
  - 클래스의 역할을 분리할 방법을 정한다
  - 분리될 역할을 담당할 클래스를 새로 만든다
  - 원래 클래스의 생성자에서 새로운 클래스의 인스턴스를 생성하여 필드에 저장
  - 분리될 역할에 필요한 필드들을 새 클래스로 옮긴다
    - 하나씩 옮길 때마다 테스트
  - 메서드들도 새 클래스로 옮긴다
    - 저수준 메서드, 즉 다른 메서드를 호출하기보다 호출을 당하는 일이 많은 메서드부터 옮긴다
    - 하나씩 옮길 때마다 테스트
  - 양쪽 클래스의 인터페이스를 살펴보면 불필요한 메서드를 제거하고, 이름도 새로운 환경에 맞게 바꾼다.
  - 새 클래스를 외부로 노출할 지 정한다
    - 노출하려거든 새 클래스에 참조를 값으로 바꾸기를 적용할 지 고민

##### 클래스 인라인하기

- 배경
  - 클래스 추출하기를 거꾸로 돌리는 리팩터링
    - 제 역할을 못하는 클래스는 인라인하자
  - 두 클래스의 기능을 현재와 다르게 배분하고 싶을 때에도 클래스를 인라인한다
    - 인라인 한 후 새로운 기능에 맞게 추출하는 것이 쉬울 때가 있다

- 절차
  - 소스 클래스의 각 public 메서드에 대응하는 메서드들을 타깃 클래스에 생성
    - 이 메서드들은 단순히 작업을 소스 클래스로 위임해야 한다
  - 소스 클래스의 메서드를 사용하는 코드를 모두 타깃 클래스의 위임 메서드를 사용하도록 변경
    - 하나씩 바꿀때마다 테스트
  - 소스 클래스의 메서드와 필드를 모두 타깃클래스로 옮긴다
    - 하나씩 옮길 때마다 테스트
  - 소스 클래스 삭제

##### 위임 숨기기

```java
Manager manager = person.getDepartment().getManager();
```
```java
class Person {
    private Department department;
    
    public Manager getManager() {
        return this.department.getManager();
    }
}
// Manager manager = person.getManager();
```

- 배경
  - 캡슐화는 모듈들이 시스템의 다른 부분에 대해 알아야 할 내용을 줄여준다
    - 무언가 변경할 때 고려해야 할 모듈 수가 적어져 코드 변경하기 쉬워진다
  - 클라이언트가 서버의 위임객체의 메서드를 사용하려면 해당 위임 객체를 알아야 한다
    - 위임 객체의 인터페이스가 변경되면 이 인터페이스를 사용하는 클라이언트 코드 모두 수정해야 한다
    - 이러한 의존성을 없애기 위해 서버 자체에 위임 메서드를 생성하고, 위임 객체의 존재를 숨기면 된다

- 절차
  - 위임객체의 각 메서드에 해당하는 위임 메서드를 서버에 생성
  - 클라이언트가 위임 객체 대신 서버를 호출하도록 수정
    - 하나씩 바꿀때마다 테스트
  - 모두 수정했다면 서버로부터 위임 객체를 얻는 접근자를 제거
  - 테스트

##### 중개자 제거하기

- 배경
  - 위임숨기기 반대 리팩터링
  - 위임 객체를 캡슐화하는 이점은 거저 주는것이 안니다
    - 위임 객체에 기능을 추가할 때마다 서버에 위임 메서드를 추가해야 하는데, 단순히 전달하는 위임 메서드가 점점 성가셔진다
  - 균형점을 적절히 유지하며 기능 추가할 때 필요 시 위임숨기기, 중개자 제거하기 리팩터링을 적용하자

- 절차
  - 위임 객체를 얻는 게터를 만든다
  - 위임 메서드를 호출하는 클라이언트가 모두 이 게터를 거치도록 수정
    - 하나씩 바꿀 때마다 테스트
  - 모두 수정했다면 위임 메서드 삭제

##### 알고리즘 교체하기

```java
public String findPersonName(People people) {
    for(Person p: people) {
        if(p.getName().equals("Don")) return p;
        else if(p.getName().equals("John")) return p;
        else if(p.getName().equals("Kent")) return p;
    }
    return new Person();
}
```
```java
public Person findPersonName(List<People> people) {
    List<String> candidates = Arrays.asList("Don", "John", "Kent");
    List<Person> people = new ArrayList<>();
    return people.stream().filter(person -> candidates.contains(person.getName()))
            .findFirst().orElse(new Person());
}
```

- 배경
  - 간명한 방법을 찾아내면 복잡한 기존 코드를 간명한 방식으로 고친다.
    - 복잡한 대상을 단순한 단위로 나눌 수 있지만, 때로는 알고리즘 전체를 걷어내고 훨씬 간결한 알고리즘으로 바꿔야 할 때가 있다
    - 문제를 더 쉽게 이해하고, 해결하는 방법을 발견하는 경우
  - 해당 작업을 착수하기 전에 가능한 잘게 나눴는지 확인해야 한다
    - 거대하고 복잡한 알고리즘을 교체하기란 어려우니 간소화하는 작업부터 교체해야 쉬어진다

- 절차
  - 교체할 코드를 함수 하나에 모든다
  - 이 함수만을 이용해 동작을 검증하는 테스트를 마련한다
  - 대체할 알고리즘을 준비한다
  - 정적 검사 수행
  - 기존 알고리즘과 새 알고리즘의 결과를 비교하는 테스트 수행
    - 두 결과가 같다면 리팩터링이 끝난다
    - 같지 않다면 기존 알고리즘을 참고하여 새 알고리즘을 테스트, 디버깅한다