package com.review.object.chap14.abstracts;

import com.review.object.chap02.entity.Money;

public interface RatePolicy {
    Money calculateFee(Phone phone);
}
