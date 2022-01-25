package com.review.refactor.chap01;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Performance {

    private String playId;

    private Integer audience;

    public Play playFor(Map<String, Play> plays) {
        return plays.get(this.getPlayId());
    }

    public int amountFor(Play play) {
        int result = 0;
        if(PlayType.TRAGEDY.equals(play.getType())) {
            result = 40000;
            if(this.getAudience() > 30) {
                result += (this.getAudience() - 30) * 1000;
            }
        } else if(PlayType.COMEDY.equals(play.getType())) {
            result = 30000;
            if(this.getAudience() > 20) {
                result += (this.getAudience() - 20) * 500 + 10000;
            }
            result += 300 * this.getAudience();
        } else {
            throw new IllegalArgumentException("알수없는 장르: " + play.getType());
        }

        return result;
    }

}
