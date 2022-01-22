package com.review.object.chap04.entity;

import com.review.object.chap02.entity.Money;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
public class Movie {

    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    /**
     * MovieType에 따라 discountAmount, discountPercent 에 데이터가 입력된다
     */
    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;

    public MovieType getMovieType() {
        return this.movieType;
    }

    /**
     * 캡슐화 문제점
     * 1. calculate... 메서드 명을를 통해 할인 정책을 노출시키고 있다.
     * 2. 만약 새로운 할인 정책이 추가된다면, 해당 메서드들에 의존하는 모든 클라이언트가 영향을 받을 것이다.
     */
    public Money calculateAmountDiscountFee() {
        if(movieType != MovieType.AMOUNT_DISCOUNT)
            throw new IllegalArgumentException();

        return fee.minus(discountAmount);
    }

    public Money calculatePercentDiscountFee() {
        if(movieType != MovieType.PERCENT_DISCOUNT)
            throw new IllegalArgumentException();

        return fee.minus(fee.times(discountPercent));
    }

    public Money calcluateNonDiscountFee() {
        if(movieType != MovieType.NONE_DISCOUNT)
            throw new IllegalArgumentException();

        return fee;
    }

    /**
     * 높은 결합도
     * 캡슐화 위반으로 인해 Movie - DiscountPolicy 사이에 높은 결합도가 발생하였다.
     * 즉, DiscountPolicy에서 변경이 발생하면 Movie에도 영향이 발생하여 수정이 필요하다
     */
    public boolean isDiscountable(LocalDateTime whenScreened, int sequence) {
        for(DiscountCondition condition : discountConditions) {
            if(condition.getType() == DiscountConditionType.PERIOD) {
                if(condition.isDiscountable(whenScreened.getDayOfWeek(), whenScreened.toLocalTime())) {
                    return true;
                }
            } else {
                if(condition.isDiscountable(sequence)){
                    return true;
                }
            }
        }

        return false;
    }

}
