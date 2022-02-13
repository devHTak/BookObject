#### 상속 다루기

##### 메서드 올리기

```java
class Employee { ... }
class SalesPerson extends  Employee {
    public String getName() {}
}
class Engineer extends  Employee {
    public String getName() {}
}
```
```java
class Employee {
    public String getName() {}
}
class SalesPerson extends  Employee { }
class Engineer extends  Employee { }
```

- 배경
  - 메서드 올리기가 가장 쉬운 상황은 메서드들의 본문코드가 똑같을 때이다
  - 메서드 올리기 리팩터링을 적용하려면 선행 단계를 거처야할 때가 많다
    - 서로 다른 두 클래스의 두 메서드를 각각 매개변수화하면 궁극적으로 같은 메서드가 되기도 한다
  - 가장 이상하고 복잡한 상황은 해당 메서드의 본문에서 참조하는 필드들이 서브클래스에만 있는 경우
    - 필드를 먼저 슈퍼클래스로 올린 후에 메서드를 올려야 한다
  - 두 메서드의 전체 흐름은 비슷하지만 세부내용이 다르다면 템플릿 메서드 만들기 고려
- 절차
  - 똑같이 동작하는 메스드인지 면밀히 살펴본다
  - 메서드 안에서 호출하는 다른 메서드와 참조하는 필드들을 슈퍼클래스에서도 호출하고 참조할 수 있는지 확인
  - 메서드 시그니처가 다르다면 함수선언바꾸기로 슈퍼클래스에서 사용하고 싶은 형태로 통일
  - 슈퍼클래스에 새로운 메서드를 생성하고, 대상 메서드의 코드를 복사해 넣는다
  - 정적 검사를 수행
  - 서브클래스 중 하나의 메서드를 제거한 후 테스트
  - 모든 서브클래스의 메서드가 없어질 때까지 다른 서브클래스의 메서드를 하나씩 제거

##### 필드 올리기

```java
class Employee { ... }
class SalesPerson extends  Employee {
    private String name;
}
class Engineer extends  Employee {
    private String name;
}
```
```java
class Employee { private String name; }
class SalesPerson extends  Employee { }
class Engineer extends  Employee { }
```

- 배경
  - 필드가 중복되기 쉬운데 이런 필드들은 이름이 비슷한 게 보통이지만 항상 그런 것은 아니다
    - 필드들은 이름이 비슷한 게 보통이지만 아닌 경우가 있다
    - 그래서 어떤 일이 벌어지는 지를 알아내려면 필드들이 어떻게 이용되는 지 분석해야 한다
  - 중복을 줄이는 방법
    - 데이터 중복 선언을 없앨 수 있다
    - 해당 필드를 사용하는 동작을 서브클래스에서 슈퍼클래스로 옮길 수 있다

- 절차
  - 후보필드들을 사용하는 곳 모두가 그 필드들을 똑같은 방식으로 사용하는지 면밀히 살핀다
  - 필드들의 이름이 각기 다르다면 똑같은 이름으로 바꾼다
    - 필드이름바꾸기
  - 슈퍼클래스에 새로운 필드 생성
  - 서브클래스의 필드 제거
  - 테스트

##### 생성자 본문 올리기

```java
class Party { }
class Employee extends Party {
    Employee(String name, Long id, Integer montlyCost) {
        this.name = name; this.id = id; this.montlyCost = montlyCost;
    }
}
```
```java
class Party { 
    public Party(String name) {
        this.name = name;
    }
}
class Employee extends Party {
    public Employee(String name, Long id, Integer montlyCost) {
        super(name);
        this.id = id; this.montlyCost = montlyCost;
    }
}
```
- 배경
  - 생성자는 하는일에 제약을 두는 편이다
  - 서브클래스들에서 기능이 같은 메서드들을 발견하면 함수추출하기, 메서드올리기를 차례로 적용하여 슈퍼클래스로 옮긴다
  - 생성자라면, 할수있는 일과 호출순서에 제약이 있기 때문에 다른 식으로 접근해야 한다
- 절차
  - 슈퍼클래스에 생성자가 없다면 정의
    - 서브클래스의 생성자들에서 해당 생성자가 호출되는지 확인
  - 문장슬라이드하기로 공통 문장 모두 super() 호출 직후로 옮긴다
  - 공통 코드를 슈퍼클래스에 추가하고 서브클래스들에서는 제거
    - 생성자 매개변수 중 공통 코드에서 참조하는 값들얼 모두 super()로 건넨다
    - 테스트
  - 생성자 시작 부분으로 옮길 수 없는 공통코드에는 함수 추출하기, 메서드올리기 적용

##### 메서드 내리기

```java
class Employee { public String getName() {} }
class SalesPerson extends  Employee { }
class Engineer extends  Employee { }
```
```java
class Employee {  }
class SalesPerson extends  Employee { }
class Engineer extends  Employee { public String getName() {} }
```

- 배경
  - 특정 서브클래스 소수에서만 관련된 메서드는 슈퍼클래스에서 제거하고 해당 서브클래스들에 추가하는 것이 좋다
  - 다만 서브클래스가 정확히 무엇인지를 호출자가 알고 있을 때만 적용할 수 있다
    - 아닌 경우 서브클래스에 따라 다르게 동작하는 슈퍼클래스의 기만적인 조건부 로직을 다형성으로 바꾸어야 한다

- 절차
  - 대상 메서드를 모든 서브클래스에 복사
  - 슈퍼클래스에서 그 메서드를 제거한 후 테스트
  - 해당 메서드를 사용하지 않는 모든 서브클래스에서 제거한 후 테스트

##### 필드 내리기

```java
class Employee { private String name; }
class SalesPerson extends  Employee { }
class Engineer extends  Employee { }
```
```java
class Employee {  }
class SalesPerson extends  Employee { }
class Engineer extends  Employee { private String name; }
```

- 배경
  - 서브클래스 소수에서만 사용하는 필드는 해당 서브클래스들로 옮긴다 
- 절차
  - 대상 필드를 모든 서브클래스에 정의
  - 슈퍼클래스에서 해당 필드 제거 후 테스트
  - 이 필드를 사용하지 않는 모든 서브클래스에서 제거한 후 테스트

##### 타입 코드를 서브클래스로 바꾸기

```java
class Employee { 
    public Employee createEmployee(String name, EmployeeType type) {
        return new Employee(name, type);
    }
}
```
```java
class Employee {
  public Employee createEmployee(String name, EmployeeType type) {
      switch(type) {
        case EmployeeType.Engineer.equals(type) : return new Engineer(name);
        case EmployeeType.Salesperson.equals(type) : return new Salesperson(name);
        case EmployeeType.Manager.equals(type) : return new Manager(name);
      }
  }
}
```
- 배경
  - 타입코드는 특정 특성에 따라 구분할 때 사용한다
  - 서브클래스가 필요할 때가 있다
    - 조건에 따라 다르게 동작하도록 해해주는 다형성을 제공
    - 특정 타입에서만 의미가 있는 값을 사용하는 필드나 메서드가 있을 때 발현된다
    
- 절차
  - 타입코드 필드를 자가 캡슐화한다
  - 타입코드 값 하나를 선택하여 그 값에 해당하는 서브클래스를 만든다
    - 타입코드 게터 메서드를 오버라이딩하여 해당 타입 코드의 리터럴 값을 반환하도록 한다
  - 매개변수로 받은 타입코드와 방금 만든 서브클래스를 매핑하는 선택 로직을 만든다
  - 테스트
  - 타입코드 값 각각에 대해 서브클래스 생성과 성택 로직 추가 반복
    - 클래스 하나 완성될 때마다 테스트
  - 타입코드 필드 제거한 후 테스트
  - 타입코드 접근자를 이용해 메서드 모두에 메서드 내리기와 조건부 로직을 다형성으로 바꾸기 적용

##### 서브클래스 제거하기

```java
class Person { 
    public GenderCode getGenderCode() {
        return GenderCode.X;
    }
}
class Male extends Person {
  public GenderCode getGenderCode() {
    return GenderCode.M;
  }
}
class Female extends Person {
  public GenderCode getGenderCode() {
    return GenderCode.F;
  }
}
```
```java
class Person {
  public GenderCode getGenderCode() {
    return this.genderCode;
  }
}
```
- 배경
  - 서브클래싱은 원래 데이터 구조와 다른 변종을 만들거나 종류에 따라 동작이 달라지게할 수 있는 유용한 메커니즘
  - 더이상 쓰이지 않는 서브클래스에 경우 서브클래스를 슈퍼클래스의 필드로 대체해 제거하는 게 최선이다
  
- 절차
  - 서브클래스의 생성자를 팩터리 함수로 바꾼다
  - 서브클래스의 타입을 검사하는 코드가 있다면 그 검사 코드에 함수 추출하기, 함수옮기기를 차례로 적용하여 슈퍼클래스로 옮긴다
    - 변경할 때마다 테스트
  - 서브클래스의 타입을 나타내는 필드를 슈퍼클래스에 만든다
  - 서브클래스를 참조하는 메서드가 방금 만든 타입 필드를 이용하도록 수정
  - 서브클래스를 지운 후 테스트

##### 슈퍼클래스 추출하기

```java
class Department {
    public int getTotalAnnualCost() {}
    public String getName() {}
    public int headCount() {}
}
class Employee {
    public int getAnnualCst() {}
    pulic String getName() {}
    public Long getId() {}
}
```
```java
class Party {
    public String getName() {}
    public int getAnnualCost() {}
}
class Department extends Party {
    public int getAnnualCost() {}
    public int headCount() {}
}
class Employee extends Party{
    public int getAnnualCst() {}
    public Long getId() {}
}
```

- 배경
  - 비슷한 일을 수행하는 두 클래스가 보이면 상속 메커니즘을 이용하여 비슷한 부분을 공통 슈퍼클래스로 옮길 수 있다
  - 슈퍼클래스 추출하기의 대안으로는 클래스 추출하기가 있다
    - 어느 것을 선택하느냐는 중복 동작을 상속으로 해결하느냐 위임으로 해결하느냐에 달렸다

- 절차
  - 빈 슈퍼클래스를 만든다
    - 원래의 클래스들이 새 클래스를 상속하도록 한다
    - 테스트
  - 생성자 본문 올리기, 메서드 올리기, 필드 올리기를 차례로 적용하여 공통 원소를 슈퍼클래스로 옮긴다
  - 서브클래스에 남은 메서드들을 검토한다
    - 공통되는 부분이 있다면 함수 추출한 다음 메서드 올리기를 적용
  - 원래 클래스들을 사용하는 코드를 검토하여 슈퍼클래스의 인터페이스를 사용하게할지 고

##### 계층합치기

- 배경
  - 클래스 계층구조를 리팩터링하다보면 기능을 위로 올리거나 아래로 내리는 일은 많이 발생한다
  - 부모가 너무 비슷해져 독립적으로 존재해야 할 이유가 사라지는 경우 합쳐야 할 시점이다
- 절차
  - 두 클래스 중 제거할 것을 고른다
  - 필드 올리기, 메서드 올리기 또는 필드 내리기, 메서드 내리기를 적용하여 모든 요소를 하나의 클래스로 옮긴다
  - 제거할 클래스를 참조하던 모든 코드가 남겨질 클래스를 참조하도록 고친다
  - 빈 클래스 제거 후 테스트
  
##### 서브클래스를 위임으로 바꾸기

```java
class Order {
    public String getDaysToShip() {
        return this.warehouse.getDaysToShip();
    }
}
class PriorityOrder extends Order {
  public String getDaysToShip() {
    return this.priorityPlan.getDaysToShip();
  }
}
```
```java
class Order {
    public String getDaysToShip() {
        return (this.priorityDelegate) ?
                    this.priorityPlan.getDaysToShip() :
                    this.warehouse.getDaysToShip();
    }
}
class PriorityOrderDelegate {
  public String getDaysToShip() {
    return this.priorityPlan.getDaysToShip();
  }
}
```

- 배경
  - 클래스에 따라 동작이 달라지는 객체들은 상속으로 표현하는 게 자연스럽다
  - 하지만 상속은 단점이 있다
    - 무언가 달라져야 하는 이유가 여러 개여도 상속에서는 그 중 단 하나의 이유만 선택해 기준으로 삼을 수 밖에 없다
    - 상속은 클래스들의 관계를 아주 긴밀하게 해준다
  - 위임으로 해결할 수 있다
    - 상속보다 결합도가 낮다
- 절차
  - 생성자를 호출하는 곳이 많다면 생성자를 팩터리 함수로 바꾼다
  - 위임으로 활용할 빈 클래스를 만든다
    - 해당 클래스의 생성자는 서브클래스에 특화된 데이터를 전부 받아야 하며,
    - 보통은 슈퍼클래스를 가리키는 역참조도 필요
  - 위임을 저장할 필드르 슈퍼클래스에 추가
  - 서브클래스 생성 코드를 수정하여 위임 인스턴스를 생성하고 위임 필드에 대입하여 초기화한다
  - 서브클래스의 메서드 중 위임 클래스로 이동할 것을 고른다
  - 함수 옮기기를 적용해 위임 클래스로 옮긴다
    - 원래 메서드에서 위임하는 코드는 지우지 않는다
  - 서브클래스 외부에도 원래 메서드를 호출하는 코드가 있다면
    - 서브클래스의 위임코드를 슈퍼클래스로 옮긴다
    - 위임이 존재하는 지를 검사하는 보호 코드로 감싸야 한다
    - 호출하는 외부 코드가 없다면 원래 메서드는 죽은 코드가 되므로 제거한다
    - 테스트
  - 서브클래스의 모든 메서드가 옮겨질 때까지 5~7 과정 반복
  - 서브클래스들의 생성자를 호출하는 코드를 찾아서 슈퍼클래스의 생성자를 사용하도록 수정한 후 테스트
  - 서브클래스를 삭제

##### 슈퍼클래스를 위임으로 바꾸기

```java
class List {}
class Stack extends List {}
```
```java
class Stack {
    public Stack() {
        this.storage = new List();
    }
}
class List {}
```

- 배경
  - 상속은 기존 기능을 재활용하는 강력하고 손쉬운 수단이다
    - 하지만 혼란과 복잡도를 키우는 방식이기도 하다
  - 하위타입의 모든 인스턴스가 상위타입의 인스턴스도 되는 등, 의미상 적합한 조건이라면 상속은 간단하고 효과적이다

- 절차
  - 슈퍼클래스 객체를 참조하는 필드를 서브클래스에 만든다
    - 위임참조를 새로운 슈퍼클래스 인스턴스로 초기화
  - 슈퍼클래스의 동작 각각에 대응하는 전달 함수를 서브클래스에 만든다
    - 서로 관련된 함수끼리 관련된 함수끼리 그룹으로 묶어 진행
    - 그룹을 하나씩 만들때마다 테스트
  - 슈퍼클래스의 동작 모두가 전달 함수로 오버라이드되었다면 상속관계를 끊는다