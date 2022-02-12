#### 데이터 조직화

##### 변수 쪼개기

- 배경
  - 변수는 다양한 용도로 사용되며 여러 번 대입할 수밖에 없는 경우도 생긴다
  - 변수는 긴 코드의 결과를 저장했다가 쉽게 참조하려는 목적으로 흔히 사용되는 데 이런 변수는 값을 단 한번만 대입하자
    - 두번이상 이뤄진다면 여러 역할을 수행한다는 신호

- 절차
  - 변수를 선언한 곳과 값을 처음 대입하는 곳에 변수 이름을 바꾼다
    - 가능하면 이 때 불변으로 선언
  - 변수에 두번째로 값을 대입하는 곳 앞까지의 모든 참조를 새로운 변수 이름으로 바꾼다
  - 두번째 대입시 변수를 원래 이름으로 다시 선언
  - 테스트
  - 마지막 대입까지 반복

##### 필드 이름 바꾸기

- 배경
  - 데이터 구조가 중요한 만큼 깔끔하게 관리해야 한다
  - 레코드의 필드 이름을 바꾸고 싶을 수 있는데, 클래스에서도 마찬가지이다

- 절차
  - 레코드의 유효범위가 제한적이라면. 필드에 접근하는 모든 코드를 수정한 후 테스트
    - 이후 단계는 필요 없다
  - 레코드가 캡슐화되지 않았다면 우선 레코드를 캡슐화한다
  - 캡슐화된 객체안의 private 필드명을 변경하고,그에 맞게 내부 메서드 변경
  - 테스트
  - 생성자의 매개변수 중 필드와 이름이 겹치는 게 있다면 함수 선언 바꾸기로 변경
  - 접근자의 이름 변경

##### 파생 변수를 질의 함수로 바꾸기

- 배경
  - 가변 데이터는 서로 다른 두 코드를 이상한 방식으로 결합하기도 한다
    - 한 쪽 코드에서 수정한 값이 연쇄 효과를 일으켜 다른 쪽 코드에 원인을 찾기 어려운 문제에 원인이 되기도 한다
  - 가변 데이터의 유효범위를 가능한 좁혀야한다
  - 값을 쉽게 계산해낼 수 있는 변수들을 모두 제거할 수 있다.
  - 예외가 존재한다
    - 변형 연산(새로운 데이터 구조 생성)
      - 데이터 구조를 감싸며 그 데이터에 기초하여 계산한 결과를 속성으로 제공하는 객체
      - 데이터 구조를 받아 다른 데이터 구조로 변환해 반환하는 함수
- 절차
  - 변수 값이 갱신되는 지점을 모두 찾는다.
    - 필요하면 변수 쪼개기를 활용해 각 갱신 지점에서 변수를 분리
  - 해당 변수의 값을 계산해주는 함수를 만든다
  - 해당 변수가 사용되는 모든곳에 어서션을 추가하여 함수의 계산 결과가 변수의 값과 같은지 확인
    - 필요하면 변수 캡슐화
  - 테스트
  - 변수를 읽는 코드를 모두 함수 호출로 변경 후 테스트
  - 변수를 선언하고 갱신하는 코드를 죽은 코드 제거하기로 없앤다

##### 참조를 값으로 바꾸기

- 배경
  - 객체(데이터 구조)를 다른 객체(데이터 구조)에 중첩하면 내부 객체를 참조 혹은 값으로 취급할 수 있다.
    - 참조로 다루는 경우엔 내부 객체는 그대로 둔 채 그 객체의 속성만 갱신
    - 값으로 다루는 경우 새로운 속성을 담은 객체로 기존 내부 객체를 통째로 대체
  - 필드를 값으로 다룬다면 내부 객체의 클래스를 수정하여 값 객체로 만들 수 있다
    - 값 객체는 대체로 자유롭게 활용하기 좋은데, 불변이기 때문이다
    - 서로 간의 참조를 관리하지 않아도 된다
  - 값 객체의 특성 때문에 리팩터링을 적용하면 안되는 상황도 있다
    - 특정 객체를 여러 객체에서 공유하고자 한다면, 객체의 값을 변경했을 때 이를 관련 객체 모두에 알려줘야 한다면 공유 객체를 참조로 다뤄야 한다

- 절차
  - 후보 클래스가 불변인지, 혹은 불변이 될 수 있는지 확인한다
  - 각각의 세터를 하나씩 제거
  - 이 값 객체의 필드들을 사용하는 동치성 비교 메서드를 만든다

##### 값을 참조로 바꾸기

- 배경
  - 값을 사용하는 경우 같은 데이터를 여러번 복사할 때 해당 값을 변경해야 할 때 모두 찾아서 변경해야 한다
    - 하나라도 놓치면 데이터 일관성이 깨빈다
  - 값을 참조로 바꾸면 엔티티 하나당 객체도 단 하나만 존재하게 되는 데, 이런 경우 클라이언트들의 접근을 관리하는 저장소가 필요하다

- 절차
  - 같은 부류에 속하는 객체들을 보관할 저장소를 만든다
  - 생성자에서 이 부류와 객체들 중 특정 객체를 정확히 찾아내는 방법이 있는지 확인
  - 호스트 객체의 생성자들을 수정하여 필요한 객체를 이 저장소에서 찾도록 한다
    - 하나 수정할 때마다 테스트

##### 매직 리터럴 바꾸기

- 배경
  - 매직 리터럴이란 소스 코드에 등장하는 일반적인 리터럴 값을 말한다
    - 3.14(PI), 9.81(standard gravity)
  - 이런 리터럴 값을 소스를 읽는 클라이언트가 의미를 모르거나 해석하기 어려울 수 있다
  - 이런 경우 상수를 사용하는 것이 좋다

- 방법
  - 상수를 선언하고 매직 리터럴을 대입
  - 해당 리터럴이 사용되는 곳을 모두 찾는다
  - 찾은 곳 각각에서 리터럴이 새 상수와 똑같은 의미로 쓰였는지 확인하여, 같은 의미라면 상수로 대체한 후 테스트한다