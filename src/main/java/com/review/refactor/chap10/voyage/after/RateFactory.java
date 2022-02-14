package com.review.refactor.chap10.voyage.after;

import com.review.refactor.chap10.voyage.History;
import com.review.refactor.chap10.voyage.Voyage;
import com.review.refactor.chap10.voyage.Zone;

import java.util.List;

public class RateFactory {

    public static Rating createRate(Voyage voyage, List<History> history) {
        if(voyage.getZone().equals(Zone.CHINA) &&
            history.stream().anyMatch(h -> h.getZone().equals(Zone.CHINA))) {
            return new ExperiencedChinaRating(voyage, history);
        }
        return new Rating(voyage, history);
    }
}
