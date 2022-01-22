package com.review.object.chap02.entity;

public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}
