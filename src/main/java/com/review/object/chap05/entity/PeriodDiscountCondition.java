package com.review.object.chap05.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class PeriodDiscountCondition implements DiscountConditionInterface{

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return this.dayOfWeek.equals(screening.getWhenScreened().getDayOfWeek()) &&
                this.startTime.compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
                this.endTime.compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
    }
}
