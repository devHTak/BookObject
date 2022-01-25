package com.review.refactor.chap01;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class Performance {

    private String playId;

    private Integer audience;

    private PerformanceCalculator performanceCalculator;

    public Performance(String playId, Integer audience) {
        this.playId = playId; this.audience = audience;
    }

    public void setPerformanceCalculator(Play play) {
        if(play.getType().equals(PlayType.COMEDY)) {
            this.performanceCalculator = new ComedyCalculator(this, play);
        } else if(play.getType().equals(PlayType.TRAGEDY)) {
            this.performanceCalculator = new TragedyCalculator(this, play);
        } else {
            throw new IllegalArgumentException("알수없는 장르: " + play.getType());
        }
    }

    public Play playFor(Map<String, Play> plays) {
        return plays.get(this.getPlayId());
    }

    public int amountFor() {
        return performanceCalculator.getAmount();
    }

}
