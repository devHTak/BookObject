package com.example.object.chap14.abstracts;

import com.example.object.chap02.entity.Money;
import com.example.object.chap14.example.Call;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicRatePolicy implements RatePolicy {

    private List<FeeRule> rules = new ArrayList<>();

    public BasicRatePolicy(FeeRule ... rules) {
        this.rules = Arrays.asList(rules);
    }

    @Override
    public Money calculateFee(Phone phone) {
        return phone.getCalls().stream()
                .map(call -> calculate(call))
                .reduce(Money.ZERO, (a, b) -> a.plus(b));
    }

    private Money calculate(Call call) {
        return rules.stream()
                .map(rule -> rule.calculateFee(call))
                .reduce(Money.ZERO, (a, b) -> a.plus(b));
    }
}
