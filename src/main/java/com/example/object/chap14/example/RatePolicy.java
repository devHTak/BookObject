package com.example.object.chap14.example;

import com.example.object.chap02.entity.Money;

public interface RatePolicy {
    Money calculateFee(Phone phone);
}
