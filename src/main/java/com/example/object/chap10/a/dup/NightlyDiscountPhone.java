package com.example.object.chap10.a.dup;

import com.example.object.chap02.entity.Money;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 심야 할인 요금제
 */
public class NightlyDiscountPhone {

    private static final int LATE_NIGHT_HOUR = 22;

    private Money nightAmount;
    private Money regularAmount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    public NightlyDiscountPhone(Money nightAmount, Money regularAmount, Duration seconds) {
        this.nightAmount = nightAmount; this.regularAmount = regularAmount; this.seconds = seconds;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        // 심야시간 통화 계산
        result.plus(calls.stream().filter(call -> call.getFrom().getHour() >= LATE_NIGHT_HOUR)
                .map(call -> nightAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()))
                .reduce((a, b) -> a.plus(b)).get());

        // 평 시간 통화 계산
        result.plus(calls.stream().filter(call -> call.getFrom().getHour() < LATE_NIGHT_HOUR)
                .map(call -> regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()))
                .reduce((a, b) -> a.plus(b)).get());

        return result;
    }
}
