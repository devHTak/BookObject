package com.example.object.chap11.composition;

import com.example.object.chap02.entity.Money;

public interface RatePolicy {
    Money calculateFee(Phone phone);
}
