package com.review.refactor.chap01;

public class ComedyCalculator extends PerformanceCalculator {

    public ComedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public int getAmount() {
        int audience = getPerformance().getAudience();
        int result = 30000;
        if(audience > 20) {
            result += (audience - 20) * 500 + 10000;
        }
        result += 300 * audience;
        return result;
    }

    @Override
    public int volumeCredits() {
        return super.volumeCredits() + (int)Math.floor(getPerformance().getAudience() / 5);
    }
}
