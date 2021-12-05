package com.example.object.chap05.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;


@NoArgsConstructor @AllArgsConstructor
public class DiscountCondition {

    private DiscountConditionType type;
    private int sequence;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    public DiscountConditionType getType() {
        return this.type;
    }


    public boolean isSatisfiedBy(Screening screening) {
        if(type == DiscountConditionType.PERIOD)
            return isSatisfiedPeriod(screening);

        return isSatisfiedSequence(screening);
    }

    private boolean isSatisfiedSequence(Screening screening) {
        return this.sequence == screening.getSequence();
    }

    private boolean isSatisfiedPeriod(Screening screening) {
        return this.dayOfWeek.equals(screening.getWhenScreened().getDayOfWeek()) &&
                this.startTime.compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
                this.endTime.compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
    }
}
