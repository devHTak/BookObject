package com.review.object.chap11.composition;

import com.review.object.chap02.entity.Money;

public interface RatePolicy {
    Money calculateFee(Phone phone);
}
