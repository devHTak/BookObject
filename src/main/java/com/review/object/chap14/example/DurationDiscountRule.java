package com.review.object.chap14.example;

import com.review.object.chap02.entity.Money;

import java.time.Duration;

/**
 * 코드 재사용을 위한 FixedFeePolicy 상속은 위험하다
 * 합성 사용 필요
 */
public class DurationDiscountRule extends FixedFeePolicy {

    private Duration from;
    private Duration to;

    public DurationDiscountRule(Money amount, Duration seconds, Duration from, Duration to) {
        super(amount, seconds);
        this.from = from;
        this.to = to;
    }

    public Money calculate(Call call) {
        if(call.getDuration().compareTo(to) > 0) {
            return Money.ZERO;
        }

        if(call.getDuration().compareTo(from) < 0) {
            return Money.ZERO;
        }

        Phone phone = new Phone();
        phone.setCall(new Call(call.getFrom().plus(from),
                call.getDuration().compareTo(to) > 0 ? call.getFrom().plus(to) : call.getTo()));

        return super.calculateFee(phone);
    }
}
