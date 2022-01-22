package com.review.object.chap14.abstracts;

import com.review.object.chap14.example.Call;
import com.review.object.chap14.example.DateTimeInterval;

import java.util.List;

public interface FeeCondition {
    List<DateTimeInterval> findTimeIntervals(Call call);
}
