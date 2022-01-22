package com.review.object.chap10.a.dup;

import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 통화 기간을 저장하는 class
 */
@AllArgsConstructor
public class Call {

    private LocalDateTime from;

    private LocalDateTime to;

    public Duration getDuration() {
        return Duration.between(from, to);
    }

    public LocalDateTime getFrom() {
        return this.from;
    }
}
