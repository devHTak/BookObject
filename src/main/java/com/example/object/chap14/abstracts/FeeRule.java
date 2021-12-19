package com.example.object.chap14.abstracts;

import com.example.object.chap02.entity.Money;
import com.example.object.chap14.example.Call;

public class FeeRule {

    private FeeCondition feeCondition;
    private FeePerDuration feePerDuration;

    public FeeRule(FeeCondition feeCondition, FeePerDuration feePerDuration) {
        this.feeCondition = feeCondition;
        this.feePerDuration = feePerDuration;
    }

    public Money calculateFee(Call call) {
        return feeCondition.findTimeIntervals(call)
                .stream()
                .map(interval -> feePerDuration.calculate(interval))
                .reduce(Money.ZERO, (a, b) -> a.plus(b));
    }
}
