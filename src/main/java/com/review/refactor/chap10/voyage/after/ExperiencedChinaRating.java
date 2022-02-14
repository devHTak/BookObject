package com.review.refactor.chap10.voyage.after;

import com.review.refactor.chap10.voyage.History;
import com.review.refactor.chap10.voyage.Voyage;

import java.util.List;

public class ExperiencedChinaRating extends Rating {

    public ExperiencedChinaRating() {
    }

    public ExperiencedChinaRating(Voyage voyage, List<History> history) {
        super(voyage, history);
    }

    @Override
    protected int captainHistoryRisk() {
        int result = super.captainHistoryRisk() - 2;
        return Math.max(result, 0);
    }

    @Override
    protected int voyageLengthFactor() {
        int result = 0;
        if(getVoyage().getLength() > 12) result += 1;
        if(getVoyage().getLength() > 18) result -= 1;

        return result;
    }

    @Override
    protected int historyLengthFactor() {
        return (this.getHistory().size()) > 10 ? 1 : 0;
    }

    @Override
    protected int voyageProfitFactor() {
        return super.voyageProfitFactor() + 3;
    }
}
