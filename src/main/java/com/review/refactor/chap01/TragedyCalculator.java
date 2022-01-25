package com.review.refactor.chap01;

public class TragedyCalculator extends PerformanceCalculator {

    public TragedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public int getAmount() {
        int result = 40000;
        if(getPerformance().getAudience() > 30) {
            result += (getPerformance().getAudience() - 30) * 1000;
        }

        return result;
    }
}
