package com.review.object.chap10.c.inherit;

import com.review.object.chap02.entity.Money;
import com.review.object.chap10.a.dup.Phone;

import java.time.Duration;

public class NightlyDiscountPhone extends Phone {

    private static final int LATE_NIGHT_HOUR = 22;

    private Money nightlyAmount;

    public NightlyDiscountPhone(Money regularAmount, Money nightlyAmount, Duration seconds) {
        super(regularAmount, seconds);
        this.nightlyAmount = nightlyAmount;
    }

    /**
     * 전체 금액 계산 후 심야할인 계산 마이너스
     * @return
     */
    @Override
    public Money calculateFees() {
        Money result = super.calculateFees();

        return result.minus(getCalls().stream().filter(call -> call.getFrom().getHour() < LATE_NIGHT_HOUR)
                .map(call -> getAmount().minus(nightlyAmount).times(call.getDuration().getSeconds() / getSeconds().getSeconds()))
                .reduce((a, b) -> a.plus(b)).get());
    }
}
