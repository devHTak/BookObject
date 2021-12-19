#### 일관성있는 협력

- 요구사항을 반복적으로 추가하거나 수정하게 되는 경우가 있다.
- 객체들의 협력 구조가 서로 다른 경우에는 코드를 이해하기도 어렵고 코드 수정으로 인해 버그가 발생할 위험성도 높아진다
- 유사한 기능을 구현하기 위해 유사한 협력 패턴을 사용하라

##### 핸드폰 과금 시스템 변경

- 기본 정책 확장
  - 고정요금 방식
  - 시간대별 방식
  - 요일별 방식
  - 구간별 방식
  - 설계
    - Phone -> (interface)RatePolicy <|-- BasicRatePolicy, AdditionalRatePolicy
    - BasicRatePolicy <|- FixedFeePolicy, TimeOfDayDiscountPolicy, DayOfWeekDiscountPolicy, DurationDiscountPolicy
    - AdditionalRatePolicy <|- RateDiscountablePolicy, TaxablePolicy

##### 설계에 일관성 부여

- 일관성 있는 설계를 만드는 데 다양한 설계 경험이 필요하다
  - 풍부한 설계 경험을 가진 사람은 어떤 변경이 중요한지, 그리고 그 변경을 어떻게 다뤄야 하는지에 대한 통찰력을 가진다.
- 널리 알려진 디자인 패턴을 학습하고 변경이라는 문맥 안에서 디자인 패턴을 적용해 보자
  - 기본 지침1. 변하는 개념을 변하지 않는 개념으로부터 분리하라
    - 각 조건문을 개별적으로 객체로 분리했고 이 객체들과 일관성있게 협력하기 위해 타입 계층을 구성
    - 이 타입 계층을 클라이언트로부터 분리하기 위해 역할을 도입하고, 최종적으로 이 역할을 추상 클래스와 인터페이스로 구현
  - 기본 지침2. 변하는 개념을 캡슐화하라
    - 훌륭한 추상화를 찾아 추상화에 의존하도록 만들자
    - 추상화에 대한 의존은 결합도를 낮추고 결과적으로 대체 가능한 역할로 구성된 협력을 설계할 수 있게 해준다

- 캡슐화
  - 캡슐화란 변하는 어떤 것이든 감추는 것
  - 대표적인 예로 퍼블릭 인터페이스와 구현을 분리하는 것
  - 다양한 종류의 캡슐화
    - 데이터 캡슐화
    - 메서드 캡슐화
    - 객체 캡슐화
    - 서브타입 캡슐화
  - 객체, 서브타입 캡슐화 방법
    - 변하는 부분을 분리하여 타입 계층을 만든다
    - 변하지 않는 부분의 일부로 타입 계층을 합성한다

##### 일관성있는 기본정책 구현

- 변경 분리하기
  - 변하는 개념과 변하지 않는 개념 분리

- 변경 캡슐화하기
  - 협력을 일관성있게 만들기 위해서 변경을 캡슐화해서 파급효과를 줄여야 한다.
  - 변경을 캡슐화하는 가장 좋은 방법은 변하지 않는 부분으로부터 변하는 부분을 분리하는 것
    - 변하지 않는 것은 규칙(FeeRule), 변하는 것은 적용조건(FeeCondition)
    - FeeCondition의 서브타입 TimeOfDayFeeCondition, DayOfWeekFeeCondition, DurationFeeCondition
- 협력 패턴 설계하기
- 추상화 수준에서 협력 패턴 구현
  - FeeCondition, FeeRule
- 구체적인 협력 구현
  - 시간대별 정책(TimeOfDayFeeCondition)
  - 요일별 정책(DayOfWeekFeeCondition)
  - 구간별 정책(DurationFeeCondition)
- 협력 패턴에 맞추기
  - 고정요금 방식(FixedFeeCondition)
