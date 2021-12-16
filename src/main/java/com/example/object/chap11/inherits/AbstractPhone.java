package com.example.object.chap11.inherits;

import com.example.object.chap02.entity.Money;
import com.example.object.chap10.a.dup.Call;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPhone {

    private List<Call> calls = new ArrayList<>();

    public Money calculateFee() {
        Money result = Money.ZERO;

        result.plus(calls.stream().map(call -> calculateCallFee(call))
                .reduce((a, b) -> a.plus(b)).get());

        return result;
    }

    protected abstract Money calculateCallFee(Call call);

    protected Money afterCalculate(Money fee) {
        return fee;
    }
}
