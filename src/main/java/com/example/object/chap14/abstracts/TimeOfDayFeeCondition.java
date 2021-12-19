package com.example.object.chap14.abstracts;

import com.example.object.chap14.example.Call;
import com.example.object.chap14.example.DateTimeInterval;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class TimeOfDayFeeCondition implements FeeCondition {

    private LocalTime from;
    private LocalTime to;

    public TimeOfDayFeeCondition(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public List<DateTimeInterval> findTimeIntervals(Call call) {
        return call.getInterval().splitByDay()
                .stream().filter(interval -> from(interval).isBefore(to(interval)))
                .map(interval -> DateTimeInterval.of(
                        LocalDateTime.of(interval.getFrom().toLocalDate(), from(interval)),
                        LocalDateTime.of(interval.getTo().toLocalDate(), to(interval))
                )).collect(Collectors.toList());
    }

    private LocalTime to(DateTimeInterval interval) {
        return interval.getTo().toLocalTime().isAfter(to)
                ? to : interval.getTo().toLocalTime();
    }

    private LocalTime from(DateTimeInterval interval) {
        return interval.getFrom().toLocalTime().isBefore(from)
                ? from : interval.getFrom().toLocalTime();
    }

}
