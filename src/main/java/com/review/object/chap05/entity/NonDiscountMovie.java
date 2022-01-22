package com.review.object.chap05.entity;

import com.review.object.chap02.entity.Money;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NonDiscountMovie extends Movie {

    @Override
    protected Money calculateDiscountAmount() {
        return Money.ZERO;
    }
}
