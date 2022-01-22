package com.review.object.chap11.inherits;

import com.review.object.chap02.entity.Money;

import java.time.Duration;

public class TaxableRegularPhone extends RegularPhone {

    private double taxRate;

    public TaxableRegularPhone(Money amount, Duration seconds, double taxRate) {
        super(amount, seconds);
        this.taxRate = taxRate;
    }

    @Override
    protected Money afterCalculate(Money fee) {
        return fee.plus(fee.times(taxRate));
    }
}
