package com.example.object.chap11.composition;

import com.example.object.chap02.entity.Money;
import com.example.object.chap10.a.dup.Call;

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
