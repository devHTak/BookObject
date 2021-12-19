package com.example.object.chap14.abstracts;

import com.example.object.chap02.entity.Money;

public interface RatePolicy {
    Money calculateFee(Phone phone);
}
