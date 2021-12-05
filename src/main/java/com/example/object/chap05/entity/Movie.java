package com.example.object.chap05.entity;

import com.example.object.chap02.entity.Money;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
public abstract class Movie {

    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;
    // private MovieType movieType;
    // private Money discountAmount;
    // private double discountPercent;

    public Money calculateMovieFee(Screening screening) {
        if(isDiscountable(screening)) {
            fee.minus(calculateDiscountAmount());
        }

        return fee;
    }

    /**
     * Movie는 DiscountCondition에게 할인 여부를 판단하라는 메시지를 전송한다.
     */
    private boolean isDiscountable(Screening screening) {
        return discountConditions.stream()
                .anyMatch(condition -> condition.isSatisfiedBy(screening));
    }

    abstract protected Money calculateDiscountAmount();

    protected Money getFee(){
        return this.fee;
    }

    /**
     * MovieType에 따라 변경되는 것을 캡슐화하라!

    private Money calculateDiscountAmount() {
        switch (movieType) {
            case PERCENT_DISCOUNT:
                return calculatePercentDiscountAmount();
            case AMOUNT_DISCOUNT:
                return calculateAmountDiscountAmount();
            case NONE_DISCOUNT:
                return calculateNoneDiscountAmount();
        }

        throw new IllegalArgumentException();
    }

    private Money calculateNoneDiscountAmount() {
        return Money.ZERO;
    }

    private Money calculateAmountDiscountAmount() {
        return discountAmount;
    }

    private Money calculatePercentDiscountAmount() {
        return fee.times(discountPercent);
    }
     */

}
