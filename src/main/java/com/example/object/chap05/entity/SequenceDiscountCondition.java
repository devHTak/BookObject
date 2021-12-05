package com.example.object.chap05.entity;

public class SequenceDiscountCondition implements DiscountConditionInterface {

    private int sequence;

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return this.sequence == screening.getSequence();
    }
}
