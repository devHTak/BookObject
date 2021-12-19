package com.example.object.chap14.abstracts;

import com.example.object.chap02.entity.Money;
import com.example.object.chap14.example.Call;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Phone {

    private Money amount;

    private Duration seconds;

    private List<Call> calls = new ArrayList<>();

    public Phone(Money amount, Duration seconds) {
        this.amount = amount; this.seconds = seconds;
    }

    public void call(Call call) {
        calls.add(call);
    }

    public List<Call> getCalls() {
        return this.calls;
    }

    public Money calculateFees() {
        Money result = Money.ZERO;

        return result.plus(calls.stream().map(call -> amount.times(call.getDuration().getSeconds() / this.seconds.getSeconds()))
                .reduce((a, b) -> a.plus(b)).get());
    }

}
