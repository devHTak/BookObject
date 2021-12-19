package com.example.object.chap14.example;

import com.example.object.chap02.entity.Money;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DayOfWeekDiscountPolicy extends BasicRatePolicy {

    private List<DayOfWeekDiscountRule> rules = new ArrayList<>();

    public DayOfWeekDiscountPolicy(List<DayOfWeekDiscountRule> rules) {
        this.rules = rules;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        Money result = Money.ZERO;

        for(DateTimeInterval interval: call.getInterval().splitByDay()) {
            for(DayOfWeekDiscountRule rule: rules) {
                result.plus(rule.calculate(interval));
            }
        }

        return result;
    }
}
