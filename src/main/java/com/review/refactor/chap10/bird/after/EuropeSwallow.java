package com.review.refactor.chap10.bird.after;

public class EuropeSwallow extends Bird {

    @Override
    public String pulmage() {
        return "보통이다";
    }

    @Override
    public int airSpeedVelocity() {
        return 35;
    }
}
