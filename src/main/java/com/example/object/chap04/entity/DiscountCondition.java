package com.example.object.chap04.entity;

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

    /**
     * 캡슐화 위반
     * 파라미터를 통해 객체에 내부 상태를 노출시킨다.
     */

    public boolean isDiscountable(DayOfWeek dayOfWeek, LocalTime time) {
        if(this.type != DiscountConditionType.PERIOD)
            throw new IllegalArgumentException();

        return this.dayOfWeek.equals(dayOfWeek) &&
                this.startTime.compareTo(time) <= 0 &&
                this.endTime.compareTo(time) >= 0;
    }

    public boolean isDiscountable(int sequence) {
        if(this.type != DiscountConditionType.SEQUENCE)
            throw new IllegalArgumentException();

        return this.sequence == sequence;
    }
}
