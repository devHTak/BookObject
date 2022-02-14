package com.review.refactor.chap10.bird.after;

public class NorwegianBlueParrot extends Bird {

    private boolean nailed;
    private int voltage;

    @Override
    public String pulmage() {
        return this.voltage > 100 ? "그을렸다" : "예쁘다";
    }

    @Override
    public int airSpeedVelocity() {
        return (this.nailed) ? 0 : 10 + this.voltage / 10;
    }
}
