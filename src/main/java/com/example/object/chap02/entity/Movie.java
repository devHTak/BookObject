package com.example.object.chap02.entity;

import lombok.AllArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
public class Movie {
    private String title;

    private Duration runningTime;

    private Money fee;

    private DiscountPolicy discountPolicy;

    public Money getFee() {
        return this.fee;
    }

    public Money calculateMovieFee(Screening screening) {
        return fee.minus(discountPolicy.caclulateDiscountAmount(screening));
    }
}
