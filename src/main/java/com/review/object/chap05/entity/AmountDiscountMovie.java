package com.review.object.chap05.entity;

import com.review.object.chap02.entity.Money;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AmountDiscountMovie extends Movie {

    private Money discountAmount;

    @Override
    protected Money calculateDiscountAmount() {
        return discountAmount;
    }
}
