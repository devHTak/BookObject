package com.review.object.chap14.example;

import com.review.object.chap02.entity.Money;

public interface RatePolicy {
    Money calculateFee(Phone phone);
}
