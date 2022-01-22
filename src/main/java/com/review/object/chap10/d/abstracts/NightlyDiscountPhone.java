package com.review.object.chap10.d.abstracts;

import com.review.object.chap02.entity.Money;
import com.review.object.chap10.a.dup.Call;
import lombok.AllArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
public class NightlyDiscountPhone extends AbstractPhone {

    private static final int LATE_NIGHT_HOUR = 22;
    private Money nightAmount;
    private Money regularAmount;
    private Duration seconds;

    @Override
    protected Money calculateCallFee(Call call) {
        if(call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
            return nightAmount.times(call.getDuration().getSeconds() / seconds.getSeconds());
        }

        return regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }
}
