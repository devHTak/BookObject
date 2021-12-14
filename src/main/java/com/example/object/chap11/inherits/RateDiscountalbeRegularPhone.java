package com.example.object.chap11.inherits;

import com.example.object.chap02.entity.Money;

import java.time.Duration;

public class RateDiscountalbeRegularPhone extends RegularPhone {
    private Money discountAmount;

    public RateDiscountalbeRegularPhone(Money amount, Duration seconds, Money discountAmount) {
        super(amount, seconds);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money afterCalculate(Money fee) {
        return fee.minus(discountAmount);
    }
}
