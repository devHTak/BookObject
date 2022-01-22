package com.review.object.chap05.entity;

import com.review.object.chap02.entity.Money;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PercentDiscountMovie extends Movie {

    private int discountPercent;

    @Override
    protected Money calculateDiscountAmount() {
        return getFee().times(discountPercent);
    }
}
