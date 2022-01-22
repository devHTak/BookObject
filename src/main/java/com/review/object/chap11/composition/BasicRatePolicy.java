package com.review.object.chap11.composition;

import com.review.object.chap02.entity.Money;
import com.review.object.chap10.a.dup.Call;

public abstract class BasicRatePolicy implements RatePolicy {

    @Override
    public Money calculateFee(Phone phone) {
        Money result = Money.ZERO;

        phone.getCalls().stream().forEach(call -> result.plus(calculateCallFee(call)));

        return result;
    }

    protected abstract Money calculateCallFee(Call call);


}
