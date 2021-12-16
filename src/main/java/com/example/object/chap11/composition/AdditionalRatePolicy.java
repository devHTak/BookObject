package com.example.object.chap11.composition;

import com.example.object.chap02.entity.Money;

public abstract class AdditionalRatePolicy implements RatePolicy{
    private RatePolicy next;
    
    public AdditionalRatePolicy(RatePolicy next) {
        this.next = next;
    }

    @Override
    public Money calculateFee(Phone phone) {
        Money fee = next.calculateFee(phone);
        return afterCalculated(fee);
    }

    protected abstract Money afterCalculated(Money fee);
}
