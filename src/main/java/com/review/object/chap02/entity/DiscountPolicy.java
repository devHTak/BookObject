package com.review.object.chap02.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DiscountPolicy {

    public List<DiscountCondition> conditions = new ArrayList<>();

    public DiscountPolicy(DiscountCondition ... discountConditions) {
        this.conditions = Arrays.asList(discountConditions);
    }

    /*
     * abstract class 정의
     * DiscountPolicy는 할인 여부와 요금 계산에 필요한 전체적인 흐름은 정의하지만 실제로 요금을 계산하는 부분은 추상 메서드인 getDiscountAmount에 위임한다
     * TODO Template METHOD
     * 부모 클래스에 기본적인 알고리즘의 흐름을 구현하고 중간에 필요한 처리를 자식에게 위임하는 디자인 패턴
     */
    public Money caclulateDiscountAmount(Screening screening) {
        return conditions.stream().filter(condition -> condition.isSatisfiedBy(screening))
                .map(condition -> this.getDiscountAmount(screening)).findFirst().orElse(Money.ZERO);
    }

    abstract protected Money getDiscountAmount(Screening screening);
}
