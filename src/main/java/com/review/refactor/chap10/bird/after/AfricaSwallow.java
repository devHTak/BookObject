package com.review.refactor.chap10.bird.after;

public class AfricaSwallow extends Bird {

    private int numberOfCoconuts;

    @Override
    public String pulmage() {
        return this.numberOfCoconuts > 2 ? "지쳤다" : "보통이다";
    }

    @Override
    public int airSpeedVelocity() {
        return 40 - (2 * this.numberOfCoconuts);
    }
}
