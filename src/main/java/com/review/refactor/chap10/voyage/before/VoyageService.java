package com.review.refactor.chap10.voyage.before;

import com.review.refactor.chap10.voyage.History;
import com.review.refactor.chap10.voyage.Voyage;
import com.review.refactor.chap10.voyage.Zone;

import java.util.Arrays;
import java.util.List;

public class VoyageService {

    public String rating(Voyage voyage, List<History> history) {
        int vpf = voyageProfitFactor(voyage, history);
        int vr = voyageRisk(voyage);
        int chr = captainHistoryRisk(voyage, history);

        if(vpf * 3 > (vr + chr * 2)) return "A";
        return "B";
    }

    private int captainHistoryRisk(Voyage voyage, List<History> history) {
        int result = 1;
        if(history.size() > 5) result += 4;
        result += history.stream().filter(h -> h.getProfit() < 0).count();
        if(voyage.getZone().equals(Zone.CHINA) && hasChina(history)) {
            result -= 2;
        }

        return Math.max(result, 0);
    }

    private int voyageRisk(Voyage voyage) {
        int result = 1;
        if(voyage.getLength() > 4) result += 2;
        if(voyage.getLength() > 8) result += voyage.getLength() - 8;

        List<Zone> zones = Arrays.asList(Zone.CHINA, Zone.EAST_CHINA);
        if(zones.contains(voyage.getZone())) {
            result += 4;
        }

        return Math.max(result, 0);
    }

    private int voyageProfitFactor(Voyage voyage, List<History> history) {
        int result = 2;
        if(voyage.getZone().equals(Zone.CHINA)) result += 1;
        if(voyage.getZone().equals(Zone.EAST_CHINA)) result += 1;
        if(voyage.getZone().equals(Zone.CHINA) && hasChina(history)) {
            result += 3;
            if(history.size() > 10) result += 1;
            if(voyage.getLength() > 12) result += 1;
            if(voyage.getLength() > 18) result -= 1;
        } else {
            if(history.size() > 8) result += 1;
            if(voyage.getLength() > 14) result -= 1;
        }

        return result;
    }

    private boolean hasChina(List<History> history) {
        return history.stream().anyMatch(h -> h.getZone().equals(Zone.CHINA));
    }
}
