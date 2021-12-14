package com.example.object.chap11.composition;

import com.example.object.chap02.entity.Money;
import com.example.object.chap10.a.dup.Call;

import java.time.Duration;

public class RegularPolicy extends BasicRatePolicy {

    private Money amount;
    private Duration seconds;

    public RegularPolicy(Money amount, Duration seconds) {
        this.amount = amount; this.seconds = seconds;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        return amount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }
}
