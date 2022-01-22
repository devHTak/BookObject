package com.review.object.chap14.abstracts;

import com.review.object.chap14.example.Call;
import com.review.object.chap14.example.DateTimeInterval;

import java.util.Arrays;
import java.util.List;

public class FixedFeeCondition implements FeeCondition {

    @Override
    public List<DateTimeInterval> findTimeIntervals(Call call) {
        return Arrays.asList(call.getInterval());
    }

}
