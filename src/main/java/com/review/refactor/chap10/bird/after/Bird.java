package com.review.refactor.chap10.bird.after;

import com.review.refactor.chap10.bird.BirdType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Bird {

    private String name;
    private BirdType birdType;

    public String pulmage() {
        return "알 수 없다";
    }

    public int airSpeedVelocity() {
        return 0;
    }
}
