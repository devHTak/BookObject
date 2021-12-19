package com.example.object.chap14.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 통화 기간을 저장하는 class
 */
public class Call {

    private DateTimeInterval interval;
    public Call(LocalDateTime from, LocalDateTime to) {
        this.interval = DateTimeInterval.of(from, to);
    }

    public Duration getDuration() {
        return interval.duration();
    }
    public LocalDateTime getFrom() { return interval.getFrom(); }
    public LocalDateTime getTo() { return interval.getTo(); }
    public DateTimeInterval getInterval() { return this.interval; }
    public List<DateTimeInterval> splitByDay() {
        return interval.splitByDay();
    }

}
