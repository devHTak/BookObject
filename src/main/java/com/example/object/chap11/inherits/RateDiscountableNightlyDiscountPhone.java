package com.example.object.chap11.inherits;

import com.example.object.chap02.entity.Money;

import java.time.Duration;

public class RateDiscountableNightlyDiscountPhone extends NightlyDiscountPhone {

    private Money discountAmount;

    public RateDiscountableNightlyDiscountPhone(Money nightAmount, Money regularAmount, Duration seconds, Money discountAmount) {
        super(nightAmount, regularAmount, seconds);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money afterCalculate(Money fee) {
        return fee.minus(discountAmount);
    }
}
