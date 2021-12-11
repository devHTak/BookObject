package com.example.object.chap10.b.code;

import com.example.object.chap02.entity.Money;
import com.example.object.chap10.a.dup.Call;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Phone {

    private static final int LATE_NIGHT_HOUR = 22;

    private PhoneType phoneType;
    private Money amount;
    private Money regularAmount;
    private Money nightlyAmount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    public Phone(Money amount, Duration seconds) {
        this.amount = amount; this.seconds = seconds;
    }

    public Phone(Money nightlyAmount, Money regularAmount, Duration seconds) {
        this.nightlyAmount = nightlyAmount; this.regularAmount = regularAmount; this.seconds = seconds;
    }

    public Phone(PhoneType phoneType, Money amount, Money nightlyAmount, Money regularAmount, Duration seconds){
        this.phoneType = phoneType; this.amount = amount;
        this.nightlyAmount = nightlyAmount; this.regularAmount = regularAmount; this.seconds = seconds;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        if(phoneType == PhoneType.REGULAR) {
            result.plus(calls.stream().map(call -> amount.times(call.getDuration().getSeconds() / seconds.getSeconds()))
                    .reduce((a, b) -> a.plus(b)).get());
        } else {
            result.plus(calls.stream().filter(call -> call.getFrom().getHour() >= LATE_NIGHT_HOUR)
                    .map(call -> nightlyAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()))
                    .reduce((a, b) -> a.plus(b)).get());

            result.plus(calls.stream().filter(call -> call.getFrom().getHour() < LATE_NIGHT_HOUR)
                    .map(call -> regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()))
                    .reduce((a, b) -> a.plus(b)).get());
        }

        return result;
    }
}
