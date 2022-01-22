package com.review.object.chap11.inherits;

import com.review.object.chap02.entity.Money;
import com.review.object.chap10.a.dup.Call;
import lombok.AllArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
public class RegularPhone extends AbstractPhone {

    private Money amount;
    private Duration seconds;

    @Override
    protected Money calculateCallFee(Call call) {
        return amount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }
}
