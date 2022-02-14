package com.review.refactor.chap10.bird.before;

import com.review.refactor.chap10.bird.BirdType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Bird {

    private String name;

    private BirdType type;

    private int numberOfCoconuts;

    private int voltage;

    private boolean nailed;

    public int airSpeedVelocity() {
        switch (this.getType()) {
            case EuropeSwallow:
                return 35;
            case AfricaSwallow:
                return 40 - (2 * this.getNumberOfCoconuts());
            case NorwegianBlueParrot:
                return (this.isNailed()) ? 0 : 10 + this.getVoltage() / 10;
            default:
                return 0;
        }
    }

    public String plumage() {
        switch (this.getType()) {
            case EuropeSwallow:
                return "보통이다";
            case AfricaSwallow:
                return this.getNumberOfCoconuts() > 2 ? "지쳤다" : "보통이다";
            case NorwegianBlueParrot:
                return this.getVoltage() > 100 ? "그을렸다" : "예쁘다";
            default:
                return "알 수 없다";
        }
    }
}
