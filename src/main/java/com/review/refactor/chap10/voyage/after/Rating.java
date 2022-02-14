package com.review.refactor.chap10.voyage.after;

import com.review.refactor.chap10.voyage.History;
import com.review.refactor.chap10.voyage.Voyage;
import com.review.refactor.chap10.voyage.Zone;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter @Setter
public class Rating {

    private Voyage voyage;
    private List<History> history;

    public Rating() {
    }

    public Rating(Voyage voyage, List<History> history) {
        this.voyage = voyage;
        this.history = history;
    }

    public String value() {
        int vpf = voyageProfitFactor();
        int vr = voyageRisk();
        int chr = captainHistoryRisk();

        if(vpf * 3 > (vr + chr * 2)) return "A";
        return "B";
    }

    protected int captainHistoryRisk() {
        int result = 1;
        if(history.size() > 5) result += 4;
        result += history.stream().filter(h -> h.getProfit() < 0).count();
        return Math.max(result, 0);
    }

    private int voyageRisk() {
        int result = 1;
        if(voyage.getLength() > 4) result += 2;
        if(voyage.getLength() > 8) result += voyage.getLength() - 8;

        List<Zone> zones = Arrays.asList(Zone.CHINA, Zone.EAST_CHINA);
        if(zones.contains(voyage.getZone())) {
            result += 4;
        }

        return Math.max(result, 0);
    }

    protected int voyageProfitFactor() {
        int result = 2;
        if(voyage.getZone().equals(Zone.CHINA)) result += 1;
        if(voyage.getZone().equals(Zone.EAST_CHINA)) result += 1;

        result += historyLengthFactor();
        result += voyageLengthFactor();
        if(history.size() > 8) result += 1;
        if(voyage.getLength() > 14) result -= 1;

        return result;
    }

    protected int voyageLengthFactor() {
        return (this.voyage.getLength() > 8) ? 1 : 0;
    }

    protected int historyLengthFactor() {
        return (this.history.size() > 14) ? -1 : 0;
    }
}
