#### 기본적인 리팩터링

##### 함수 추출하기

```java

public void printOwing(Invoice invoice) {
    printBanner();
    int outstanding = calculateOutstanding();
    
    // 세부사항 출력
    log.info("고객명: {}", invoice.getCustomer());
    log.info("채무액: {}", outstanding);
}

public void printOwing(Invoice invoice) {
    printBanner();
    int outstanding = calculateOutstanding();
    
    printDetails(invoice, outstanding);  
}

private void printDetails(Invoice invoice, int outstanding) {
    log.info("고객명: {}", invoice.getCustomer());
    log.info("채무액: {}", outstanding);
}
```
- 배경
  - 코드 조각을 찾아 무슨 일을 하는지 파악한 다음, 독립된 함수로 추출하고 목적에 맞는 이름을 붙인다
  - 독립된 함수로 추출하는 기준 1. 길이
  - 독립된 함수로 추출하는 기준 2. 재사용성
  - 독립된 함수로 추출하는 기준 3. 목적과 구현을 분리
  - 짧은 함수의 이점을 잘 살리기 위해서는 이름을 잘 지어야 한다
    - 문서 없이 코드 자체만으로 내용을 충분히 설명할 수 있다
    
- 절차
  - 함수를 새로 만들고 목적을 잘 드러내는 이름을 붙인다
    - 어떻게가 아닌 무엇을 하는지가 드러나야 한다
    - 이름이 떠오르지 않는다면 함수로 추출하면 안된다는 신호다
  - 추출할 코드를 원본 함수에서 복사하여 새 함수에 붙여넣는다
  - 추추란 코드 중 원본 함수의 지역 변수를 참조하거나 추출한 함수의 유효범위를 벗어나는 변수는 없는 지 검사한다. 
    - 있다면 매개변수로 전달한다
    - 추출한 코드 안에서 값이 바뀌는 변수 중에서 값으로 전달되는 것들은 주의해서 처리한다
      - 이런 변수가 하나분이라면 추출한 코드를 질의 함수로 취급하여 그 결과를 반환하여 대입하도록 한다
      - 만약 이런 변수가 많다면 함수 추출을 멈추고 변수 쪼개기, 임시 변수를 질의 함수로 바꾸기와 같은 다른 리팩터링을 적용하여 변수를 사용하는 코드를 단순하게 바꾼 후 다시 함수 추출을 시도하자
  - 변수를 다 처리했다면 컴파일한다
  - 원본 함수에서 추출한 코드 부분을 새로 만든 함수를 호출하는 문장으로 바꾼다
    - 즉, 추출한 함수로 일을 위임한다
  - 테스트한다
  - 다른 코드에 방금 추출한 것과 똑같거나 비슷한 코드가 없는지 살핀다
    - 있다면 방금 추출한 새 함수를 호출하도록 바꿀지 검토한다 (인라인 코드를 함수 호출로 바꾸기)

- 예시
  - package com.review.refactor.chap06.ExtractFunction.java

##### 함수 인라인하기

```java
public int getRating(Driver driver) {
    return moreThanFiveLateDelivers(driver) ? 2 : 1;
}

public boolean moreThanFiveLateDelivers(Driver driver) {
    return driver.getNmberOfLateDelivers() > 5;
}

public int getRating(Driver driver) {
    return (drver.getNmberOfLateDelivers > 5) ? 2 : 1;
}
```

- 배경
  - 리팩터링과정에서 잘못 추출된 함수들은 다시 인라인한다
    - 때로는 함수 본문이 이름만큼 명확한 경우가 있거나 함수 본문 코드를 이름만큼 깔끔하게 리팩터링할 때도 있다
  - 간접 호출을 과하게 사용하는 경우 흔한 인라인 대상이 된다

- 절차
  - 다형 메서드인지 확인
    - 서브클래스에서 오버라이드하는 메서드는 인라인하면 안된다
  - 인라인할 함수를 호출하는 곳을 모두 찾는다
  - 각 호출문을 함수 본문으로 교체
  - 하나씩 교체할 때마다 테스트
    - 인라인하긱 까다로운 부분이 있다면 일단 남겨두고 여유가 생길 때 마다 틈틈히 처리
  - 함수 정의(원래 함수)를 삭제

##### 변수 추출하기

```java
public int calculatePrice(Order order) {
    return order.getQuantity * order.getItemPrice()
        - Math.max(0, order.getQuantity - 500) * order.getItemPrice * 0.05
        + Math.min(order.getQuantity * order.getItemPrice * 0.1, 100);
}

public int calculatePrice(Order order) {
    int basePrice = order.getQuantity * order.getItemPrice();
    int quantityDiscount = Math.max(0, order.getQuantity - 500) * order.getItemPrice * 0.05;
    int shipping = Math.min(order.getQuantity * order.getItemPrice * 0.1, 100);
    return basePrice - quantityDiscount + shipping;
}
```

- 배경
  - 표현식이 너무 복잡하여 어려운 경우 지역 변수를 활용하여 표현식을 쪼개 관리하기 더 쉽게 만들 수 있다
  - 표현식에 이름을 붙일 때에는 이름이 들어갈 문맥도 고려하자

- 절차
  - 추출하려는 표현식에 부작용은 없는지 확인
  - 불변 함수를 하나 선언하고 이름을 붙일 표현식의 복제본을 대입
  - 원본 표현식을 새로 만든 변수로 교체
  - 테스트
  - 표현식을 여러 곳에서 사용한다면 각각을 새로 만든 변수로 교체
    - 하나 교체할 때마다 테스트

##### 변수 인라인하기

```java
int basePrice = order.getBasePrice();
return basePrice > 1000;

return order.getBasePrice() > 1000;
```

- 배경
  - 변수추출한 이름이 원래 표현식과 다를 바 없는 경우 변수가 주변 코드를 리팩터링하는 데 방해가 되기도 한다

- 절차
  - 대입문의 우변에서 부작용이 생기는지 확인
  - 변수가 불변으로 선언되지 않았다면 불변으로 반든 후 테스트
    - 이렇게 하는 경우 변수에 값이 단 한번만 대입되는지 확인 가능
  - 이 변수를 가장 처음 사용하는 코드를 찾아 대입문의 우변 코드로 변경
  - 테스트
  - 변수 사용하는 부분을 모두 교체하여 테스트 진행
  - 변수 선언문과 대입문 삭제
  - 테스트

##### 함수 선언 바꾸기

```java
public void circum(int radius) {}

public void circumference(int radius) {}
```

- 배경
  - 함수에서 가장 중요한 것은 이름이다
    - 이름이 좋으면 함수의 구현 코드를 살펴볼 필요없이 호출문만 보고 무슨일을 하는지 파악할 수 있다
    - 주석을 작성하다 보면 좋은 이름이 떠오를 때가 있다
  - 매개변수는 함수를 사용하는 문맥을 설정한다

- 절차
  - 간단한 절차
    - 매개변수를 제거하려거든 먼저 함수 본문에서 제거 대상 매개변수를 참조하는 곳은 없는지 확인
    - 메서드 선언을 원하는 형태로 변경
    - 기존 메서드 선언을 참조하는 부분을 모두 찾아 바뀐 형태로 수정
    - 테스트
  - 마이그레이션 절차
    - 이어지는 추출 단계를 수월하게 만들어야 한다면 함수의 본문을 적절히 리팩터링
    - 함수 본문을 새로운 함수로 추출
      - 새로 만들 함수 이름이 기존 함수와 같다면 일단 검색하기 쉬운 이름을 임시로 붙인다
    - 추출한 함수에 매개변수를 추가해야 한다면 간단한 절차를 따라 추가
    - 테스트
    - 기존 함수를 인라인
    - 이름을 임시로 붙여줬다면 함수 선언 바꾸기를 한 번 더 적용해 원래 이름으로 되돌린다
    - 테스트

##### 변수 캡슐화하기

- 배경
  - 데이터는 참조하는 모든 부분을 한번에 바꾸어야 코드가 제대로 작동한다
    - 짧은 함수 안의 임시 변수처럼 유효범위가 아주 좁은 데이터는 어렵지 않다
    - 유효범위가 넓어질수록 다루기 어려워진다
  - 데이터를 변경하고 사용하는 코드를 감시할 수 있는 확실한 통로가 되어주기 때문에 데이터 변경 전 검증이나 변경 후 추가 로직을 쉽게 끼워넣을 수 있다
  - 불변 데이터는 가변 데이터보다 캡슐화할 이유가 적다
    - 데이텉 변경될 일이 없어서 갱신 전 검증같은 추가 로직이 필요가 없기 때문이다

- 절차
  - 변수로의 접근과 갱신을 전담하는 캡슐화 함수들을 만든다
  - 정적 검사를 수행
  - 변수를 직접 참조하던 부분을 모두 적절한 캡슐화 함수 호출로 바꾼다
    - 하나씩 변경할 때마다 테스트
  - 변수의 접근 범위를 제한
    - 변수로의 직접 접근을 막을 수 없을 때도 있다. 그럴때는 변수 이름을 바꾸어 테스트하면 변수를 참조하는 곳을 쉽게 찾을 수 있다
  - 테스트
  - 변수 값이 레코드라면 레코드 캡슐화하기를 적용할지 고려해보자

##### 변수 이름 바꾸기

```java
int a = height * width;

int area = height * width;
```

- 배경
  - 명확한 프로그래밍의 핵심은 이름짓기다
    - 변수명은 하려는 일에 관해 많은 것을 설명해준다.
  - 이름의 중요성은 그 사용 범위에 영향을 많이 받는다
  - 함수 호출 한번으로 끝나지 않고, 값이 영속되는 필드라면 이름에 더 신경쓰자

- 절차
  - 폭넓게 쓰이는 변수라면 변수 캡슐화하기를 고려한다
  - 이름을 바꿀 변수를 참조하는 곳을 모두 찾아서, 하나씩 변경
    - 변수값이 변하지 않는다면 다른 이름으로 복제본을 만들어 점진적으로 변경, 변경할 때마다 테스트 진행
  - 테스트
  
##### 매개변수 객체 만들기

```java
public void amountInvoiced(LocalDateTime startTime, LocalDateTime endTime) {}

public void amountInvoiced(Invoice invoice) {}

```
- 배경
  - 데이터 항목 여러 개가 여러 함수로 함께 몰려다니는 경우, 데이터 구조를 하나로 모아주자
    - 데이터 사이에 관계가 명확해지며 매개변수의 수가 줄어든다
    - 데이터 구조를 새로 발견하면 데이터 구조를 활용하는 형태롤 프로그램 동작을 재구성한다

- 절차
  - 적당한 데이터 구조가 아직 마련되어 있지 않다면 새로 만든다
  - 테스트
  - 함수 선언 바꾸기로 새 데이터 구조를 매개변수로 추가
  - 테스트
  - 함수 호출 시 새로운 데이터 구조 인스턴스를 넘기도록 수정
    - 변경할 때마다 테스트
  - 기존 매개변수를 사용하던 코드를 새 데이터 구조의 원소를 사용하도록 바꾼다
  - 다 바꿨다면 기존 매개변수를 제거하고 테스트
  
##### 여러 함수를 클래스로 묶기

```java
public void base(Reading reading) {}
public void texableCharge(Reading reading) {}
public void calculateBaseCharging(Reading reading) {}

class Reading {
    public void base() {}
    public void texableCharge() {}
    public void calculateBaseCharging() {}
}
```

- 배경
  - 클래스는 데이터와 함수를 하나의 공유 환경으로 묶은 후, 다른 프로그램 요소와 어우러질 수 있도록 그중 일부를 외부에 제공
  - 공통 데이터를 중심으로 긴밀하게 엮어 작동하는 함수 무리를 발견하면 클래스로 묶자
    - 클래스로 묶으면 함수들이 공유하는 공통 환경을 명확하게 표현할 수 있고, 각 함수에 전달되는 인수를 객체안에서 함수 호출로 간결하게 만들 수 있다
  - 이미 만들어진 함수를 재구성할 때, 새로 만든 클래스와 관련하여 놓치는 연산을 찾아서 새로운 메서드로 뽑아내는 데 좋다
    - 클래스로 묶을 때의 두드러진 장점은 클라이언트가 객체의 핵심 데이터를 변경할 수 있고, 파생 객체들을 일관되게 관리할 수 있다는 것이다.

- 절차
  - 함수들이 공유하는 공통 데이터 레코드를 캡슐화한다
    - 공통데이터가 레코드 구조로 묶여있지 않다면 객체 만들기로 데이터를 하나로 묶는 레코드를 만든다
  - 공통 레코드를 사용하는 함수 각각을 새 클래스로 옮긴다
  - 데이터를 조작하는 로직들은 함수로 추출해서 새 클래스로 옮긴다

##### 여러 함수를 변환 함수로 묶기

```java
public void base(Reading reading) {}
public void texableCharge(Reading reading) {}

public Reading enrichReading(Reading reading) {
    Reading result = new Reading();
    result.setBaseCharge(base(reading));
    result.setTexableCharge(texableCharge(reading));
    
    return result;
}
```

- 배경
  - 소프트웨어는 여러 데이터를 입력받아 여러 가지 정보로 도출한다
    - 도출하는 로직은 반복되어 사용될 수 있다
    - 이런 도출되는 작업을 한데 모아두면 검색, 갱신을 일관된 장소에서 처리할 수 있고, 로직 중복도 막을 수 있다.
  - 변환함수(transform) 사용
    - 원본 데이터를 입력받아 정보를 도출한 뒤, 각각을 출력 데이터의 필드에 넣어 반환
    - 도출 과정을 검토할 일이 생겼을 때 변환 함수만 살펴보면 된다
  - 변환함수 사용 대신 클래스로 묶기를 사용해도 된다
  - 변환함수 vs 클래스 묶기 차이점
    - 원본데이터가 수정되는 경우에는 클래스 묶기를 사용하는 것이 좋다
    - 변환함수를 사용하는 경우 가공한 데이터를 새로운 레코드에 담기 때문에 일관성이 깨질 수 있다

- 절차
  - 변환할 레코드를 입력받아서 값을 그대로 반환하는 변환 함수를 만든다
    - 깊은 복사로 처리해야 한다.
    - 원본 레코드를 바꾸지 않는지 확인(테스트) 필요
  - 묶을 함수 중 함수 하나를 골라서 본문 코드를 변환 함수로 옮기고, 처리 결과를 레코드에 새 필드로 기록, 클라이언트 코드가 해당 필드를 사용하도록 수정
  - 테스트
  - 나머지 관련 함수에 과정 반복하여 적용

##### 단계 쪼개기

```java
public int getOrderPrice(String orderString) {
    String[] orderData = orderString.split(/\s+/);
    int productPrice = priceMap.get(orderData[0].split("-")[1]);
    int orderPrice = Integer.parseInt(orderData[1]) * productPrice;
    
    return orderPrice;
}

public int getOrderPrice(String orderString) {
    Order order = parseOrder(orderString);
    int orderPrice = price(order, priceMap);
    
    return orderPrice;
}

private Order parseOrder(String orderString) {
    String[] orderData = orderString.split(/\s+/);
    Order order = new Order();
    order.setProductId(orderData[0].split["-"][1]);
    order.setQuantity(Integer.parseInt(orderData[1]));
    
    return order;
}

private int price(Order order, Map<String, Integer> priceMap) {
    return order.getQuantity() * priceMap.get(order.getProductId);
}
```

- 배경
  - 서로 다른 두 대상을 한꺼번에 다루는 코드를 발견하면 각각을 별개 모듈로 나누는 방법을 모색하자
    - 두 대상을 동시에 생각할 필요 없이, 하나에만 집중이 가능하다
  - 가장 간편한 방법은 하나의 동작을 두단계로 쪼개는 것

- 절차
  - 두번째 단계에 해당하는 코드를 독립 함수로 추출
  - 테스트
  - 중간 데이터 구조를 만들어 앞에서 추출한 함수의 인수로 추가
  - 테스트
  - 추출한 두번째 단계 함수의 매개변수를 하나씩 검토
    - 첫번째 단계에서 사용되는 것은 중간 데이터 구조로 옮긴다. 옮길 때마다 테스트
  - 첫번째 단계 코드를 함수로 추출하면서 중간 데이터 구조를 반환하도록 만든다
