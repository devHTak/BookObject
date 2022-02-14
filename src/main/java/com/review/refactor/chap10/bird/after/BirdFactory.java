package com.review.refactor.chap10.bird.after;

public class BirdFactory {

    public static Bird createBird(Bird bird) {
        switch (bird.getBirdType()){
            case EuropeSwallow:
                return new EuropeSwallow();
            case AfricaSwallow:
                return new AfricaSwallow();
            case NorwegianBlueParrot:
                return new NorwegianBlueParrot();
            default:
                return new Bird();
        }
    }
}
