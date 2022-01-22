package com.review.object.chap10.a.dup;

import com.review.object.chap02.entity.Money;
import lombok.Getter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 통화 요금 계산
 */
@Getter
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

    public Money calculateFees() {
        Money result = Money.ZERO;

        return result.plus(calls.stream().map(call -> amount.times(call.getDuration().getSeconds() / this.seconds.getSeconds()))
                .reduce((a, b) -> a.plus(b)).get());
    }
}
