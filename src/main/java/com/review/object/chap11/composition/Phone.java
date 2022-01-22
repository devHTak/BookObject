package com.review.object.chap11.composition;

import com.review.object.chap02.entity.Money;
import com.review.object.chap10.a.dup.Call;

import java.util.ArrayList;
import java.util.List;

public class Phone {
    private RatePolicy ratePolicy;

    private List<Call> calls = new ArrayList<>();

    public Phone(RatePolicy ratePolicy) {
        this.ratePolicy = ratePolicy;
    }

    public List<Call> getCalls() {
        return this.calls;
    }

    public Money calculateFee() {
        return ratePolicy.calculateFee(this);
    }
}
