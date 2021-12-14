package com.example.object.chap11.inherits;

import com.example.object.chap02.entity.Money;

import java.time.Duration;

public class TaxableNightlyDiscountPhone extends NightlyDiscountPhone {

    private double taxRate;

    public TaxableNightlyDiscountPhone(Money nightlyAmount, Money regularAmount, Duration seconds, double taxRate) {
        super(nightlyAmount, regularAmount, seconds);
        this.taxRate = taxRate;
    }

    @Override
    protected Money afterCalculate(Money fee) {
        return fee.plus(fee.times(taxRate));
    }
}
