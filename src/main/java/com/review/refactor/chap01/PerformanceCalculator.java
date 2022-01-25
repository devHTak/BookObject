package com.review.refactor.chap01;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor @AllArgsConstructor
public abstract class PerformanceCalculator {

    private Performance performance;
    private Play play;

    public abstract int getAmount();

    public int volumeCredits() {
        return Math.max(performance.getAudience() - 30, 0);
    }
}
