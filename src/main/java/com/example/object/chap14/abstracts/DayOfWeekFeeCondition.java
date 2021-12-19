package com.example.object.chap14.abstracts;

import com.example.object.chap14.example.Call;
import com.example.object.chap14.example.DateTimeInterval;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DayOfWeekFeeCondition implements FeeCondition {

    private List<DayOfWeek> dayOfWeeks = new ArrayList<>();

    public DayOfWeekFeeCondition(DayOfWeek ... dayOfWeeks) {
        this.dayOfWeeks = Arrays.asList(dayOfWeeks);
    }

    @Override
    public List<DateTimeInterval> findTimeIntervals(Call call) {
        return call.getInterval().splitByDay().stream()
                .filter(interval -> dayOfWeeks.contains(interval.getFrom().getDayOfWeek()))
                .collect(Collectors.toList());
    }
}
