package com.review.object.chap14.example;

import com.review.object.chap02.entity.Money;

import java.time.Duration;

public class Phone {

    private Money amount;

    private Duration seconds;

    private Call call;

    public Phone() {}
    public Phone(Money amount, Duration seconds, Call call) {
        this.amount = amount; this.seconds = seconds; this.call = call;
    }

    public Call getCall(){
        return this.call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public Money calculateFee() {
        return Money.ZERO.plus(amount.times(call.getDuration().getSeconds() / this.seconds.getSeconds()));
    }

}
