#### 조건부 로직 간소화

##### 조건문 분해하기

```java
if(!now.isBefore(plan.getSummerStart) && !now.isAfter(plan.getSummerEnd()) {
	charge = quntity * plan.getSummerRate();
} else {
	charge = quantity * plan.getRegularRate() * plan.getRegularServiceCharge();
}
```
```java
if(isSummer()) {
	charge = summerCharge();
} else {
	charge = regularCharge();
}
```
- 배경
  - 다양한 조건과 그에 따른 동작도 다양한 코드를 작성하면 긴 함수가 탄생한다.
  - 거대한 코드 블록이 주어지면 코드를 부위별로 분해한 다음 해체된 코드 덩어리들을 각 덩어리 의도를 살린 이름의 함수 호출로 변경하면 의도가 더 확실히 드러난다
		
- 절차
  - 조건식과 그 조건식에 딸린 조건절 각각을 함수로 추출

##### 조건식 통합하기
```java
if(employee.getSeniority() < 2) return 0;
if(employee.getMontsDisabled() > 12) return 0;
if(employee.isPartTime()) return 0;
```
```java
if(isNotEligibleForDisability()) return 0;

private boolean isNotEligibleForDisability() {
	return ( (employee.getSeniority() < 2) || (employee.getMontsDisabled() > 12) || ( employee.isPartTime());
}
```

- 배경
  - 비교하는 조건은 다르지만 그 결과로 수행하는 동작이 똑같은 경우에는 조건 검사를 하나로 통합하는 것이 좋다
    - 여러 조각으로 나뉜 조각들을 하나로 통합함으로써 내가 하려는 일이 더 명확해진다.
    - 해당 작업이 함수 추출하기까지 이어질 가능성이 높다
  - 만약 독립된 검사로 판단되면 해당 리팩터링은 진행하면 안된다
  
- 절차
  - 해당 조건식들 모두에 부수효과가 없는지 확인한다
    - 부수효과가 있는 조건식에는 질의 함수와 변경함수 분리하기를 먼저 적용
  - 조건문 두 개를 선택하여 두 조건문의 조건식들을 논리 연산자로 결합
  - 테스트
  - 조건이 하나만 남을 때까지 2~3을 반복
  - 하나로 합쳐진 조건식을 함수로 추출할 지 고려

##### 중첩 조건문을 보호 구문으로 바꾸기

```java
public int getPayAmount() {
	int result;
	if(isDead) {
		result = deadAmount();
	} else {
		if(isSeperated) {
			result = seperateAmount();
		} else {
			if(isRetired) {
				result = retiredAmount();
			} else {
				result = normalPayAmount();
			}
		} 
	}
	return result;
}
```
```java
public int getPayAmount() {
	if(isDead) return deadAmount();
	if(isSeperated) return seperateAmount();
	if(isRetired) return retiredAmount();
	return normalPayAmount();
}
```

- 배경
  - 조건문은 참인 경로와 거짓인 경로(if, else)와 한쪽만 정상인 형태로 존재한다
    - 한쪽만 정상인 검사 형태를 보호 구문(guard clause)라고 한다
  - 중첩 조건문을 보호구문으로 바꾸는 리팩터링의 핵심은 의도를 부각하는 목적이다
    - 중첩조건(If-else)을 사용하면 똑같이 무게 중심을 두게 된다
    - 보호 구문을 사용하면 조건은 해당 함수의 핵심이 아니며 무언가 조치를 취한 후 함수에서 빠져나온다라고 이야기한다

- 절차
  - 교체해야 할 조건 중 가장 바깥것을 선택하여 보호 구문으로 바꾼다
  - 테스트
  - 1 ~ 2 과정을 필요한만큼 반복한다.
  - 모든 보호 구문이 같은 결과를 반환한다면 보호 구문들의 조건식을 통합한다

##### 조건부 로직을 다형성으로 바구기

```java
switch(bird.getType()) {
	case EuropeSwallow:
		return "normal";
	case AafricaSwallow:
		return (bird.getNumberOfCoconuts() > 2) ? "tired" : "normal";
	case NorwegianBlueParrot:
		return (bird.getVoltage() > 100) ? "tanned skin" : "good";
	default:
		return "unknown";
}
```
```java
class EuropeSwallow {
	public String getPlumage() {
		return "normal";
	}
}
class AafricaSwallow {
	public String getPlumage() {
		return (this.getNumberOfCoconuts() > 2) ? "tired" : "normal";
	}
}
class NorwegianBlueParrot {
	public String getPlumage() {
		return (this.getVoltage() > 100) ? "tanned skin" : "good";
	}
}
```
- 배경
  - 조건부 로직을 직관적으로 구조화할 방법으로 클래스와 다형성을 이용하면 더 확실하게 분리할 수 있다
  - 조건부로 분기되는 로직을 슈퍼클래스에 기본 메서드로 정의하고 변형 동작에 맞게 서브클래스로 만든다
    - 해당 서브클래스들은 기본 동작과의 차이를 표현하는 코드로 채워진다

- 절차
  - 다형적 동작을 표현하는 클래스들이 아직 없다면 만들어준다
  - 호출하는 코드에서 팩터리 함수 사용
  - 조건부 로직함수를 슈퍼클래스로 옮긴다
  - 서브 클래스 중 하나 선택
    - 서브 클래스에서 슈퍼클래스의 조건부 로직 메서드를 오버라이드 한다
    - 조건부 문장 중 선택된 서브클래스에 해당하는 조건절을 서브클래스 메서드로 복사한 다음 수정
  - 같은 방식으로 각 조건절을 해당 서브클래스에서 메서드로 구현
  - 슈퍼클래스 메서드에는 기본 동작 부분만 남긴다
    - 혹은 슈퍼클래스가 추상클래스이면 해당 메서드를 추상으로 선언하거나 서브클래스에서 처리해야 함을 알리는 에러를 던진다

- 예제
  - 예제 1. package com.review.refactor.chap10.bird.*
  - 예제 2. package com.review.refactor.chap10.voyage.*

##### 특이 케이스 추가하기

```java
if(customer.getName().equals("미확인 고객")) customerName = "거주자";
```
```java
class UnknownCustomer {
	public String getName() {
		return "거주자";
	}
}
```

- 배경
  - 코드베이스에서 특정 값에 대해 똑같이 반응하는 코드가 여러 곳이라면 해당 반응들을 한 데 모으는게 효율적이다
  - 특이 케이스 패턴: 특수한 경우의 공통 동작을 요소 하나에 모아서 사용하는 패턴
    - 특이 케이스 객체는 캡슐화한 클래스가 반환하도록 만들 수 있고, 변환을 거쳐 데이터 구조에 추가시키는 형태가 될 수 있다
  - 널 객체 패턴
    - 널은 특이 케이스로 처리할 경우가 많으며 특이 케이스의 특수한 예

- 절차
  - 컨테이너에 특이 케이스인지를 검사하는 속성을 추가하고, false를 반환하게 한다
  - 특이 케이스 객이를 만든다. 이 객체는 특이 케이스인지를 검사하는 속성만 포함하며, 이 속성은 true를 반환하도록 한다
  - 클라이언트에서 특이 케이스인지를 검사하는 코드를 함수로 추출한다. 모든 클라이언트가 값을 직접 비교하는 대신 방금 추출한 함수를 사용하도록 고친다
  - 코드에 새로운 특이 케이스 대상을 추가한다. 함수의 반환 값으로 받거나 변환 함수를 적용하면 된다.
  - 특이 케이스를 검사하는 함수 본문을 수정하여 특이 케이스 객체의 속성을 사용하도록 한다
  - 테스트
  - 여러 함수를 클래스로 묶기나 여러 함수를 변환함수로 묶기를 적용하여 특이 케이스를 처리하는 공통 동작을 새로운 요소로 옮긴다
  - 아직도 특이 케이스 검사 함수를 이용하는 곳이 남아 있다면 검사 함수를 인라인한다

##### 제어 플래그를 탈출문으로 바꾸기

```java
for(const p of people) {
	if(!found) {
		if( p.getName().equals("조커")) {
			sendAlert();
			found = true;
		}
	}
}
```
```java
for(const p of people) {
	if( p.getName().equals("조커")) {
		sendAlert();
		found = true;
	}
}
```

- 배경
  - 제어 플래그란 코드의 동작을 변경하는 데 사용하는 변수를 말하며, 어딘가에서 갑슬 계산해 제어 플래그에 설정한 후, 다른 어딘가의 조건문에서 검사하는 형태로 쓰인다.
  - 리팩터링을 통해 충분히 간소화할 수 있다.
    - break, continue, return 등을 활용할 수 있다

- 절차
  - 제어 플래그를 사용하는 코드를 함수로 추출할 지 고려한다
  - 제어 플래그를 갱신하는 코드 각각을 적절한 제어문으로 바꾼다. 하나씩 테스트
  - 모두 수정했다면 제어 플래그를 제거
