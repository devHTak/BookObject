package com.review.refactor.chap01;

import com.review.refactor.chap01.Performance;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class Statement {
    private int totalAmount;
    private int totalVolumeCredits;
    private List<Performance> performances;
    private String customer;

    public Statement() {
        this.performances = new ArrayList<>();
    }

    public void setPerformances(List<Performance> performances) {
        performances.forEach(performance -> this.performances.add(performance));
    }

    public void createStatement(Invoice invoice, Map<String, Play> plays){
        this.setCustomer(invoice.getCustomer());
        this.setPerformances(invoice.getPerformances());
        this.setTotalAmount(totalAmount(invoice.getPerformances(), plays));
        this.setTotalVolumeCredits(totalVolumeCredits(invoice.getPerformances(), plays));
    }

    private int totalAmount(List<Performance> performances, Map<String, Play> plays) {
        return performances.stream()
                .map(performance -> performance.amountFor())
                .reduce(0, (sub, element) -> sub + element);
    }

    private int totalVolumeCredits(List<Performance> performances, Map<String, Play> plays) {
        int result = 0;
        for(Performance performance: performances) {
            result += volumeCreditsFor(performance, performance.playFor(plays));
        }

        return performances.stream()
                .map(performance -> volumeCreditsFor(performance, performance.playFor(plays)))
                .reduce(0, (sub, element) -> sub + element);
    }

    private int volumeCreditsFor(Performance performance, Play play) {
        int result = Math.max(performance.getAudience() - 30, 0);
        if(PlayType.COMEDY.equals(play.getType())) {
            result += Math.floor(performance.getAudience() / 5);
        }

        return result;
    }

}
