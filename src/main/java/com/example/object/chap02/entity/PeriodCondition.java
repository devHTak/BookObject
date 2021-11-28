package com.example.object.chap02.entity;

import lombok.AllArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@AllArgsConstructor
public class PeriodCondition implements DiscountCondition {

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return screening.getStartTime().getDayOfWeek().equals(dayOfWeek) &&
                startTime.compareTo(screening.getStartTime().toLocalTime()) <= 0 &&
                endTime.compareTo(screening.getStartTime().toLocalTime()) >= 0;
    }
}
