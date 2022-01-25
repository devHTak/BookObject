package com.review.refactor.chap01;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Bill {

    public String statement(Invoice invoice, Map<String, Play> plays) {
        int totalAmount = 0;
        int volumeCredits = 0;
        String printBill = "청구 내역 (고객명: " + invoice.getCustomer() + ")\n";

        for(Performance performance: invoice.getPerformances()) {
            Play play = plays.get(performance.getPlayId());
            int thisAmount = 0;

            if(PlayType.TRAGEDY.equals(play.getType())) {
                thisAmount = 40000;
                if(performance.getAudience() > 30) {
                    thisAmount += (performance.getAudience() - 30) * 1000;
                }
            } else if(PlayType.COMEDY.equals(play.getType())) {
                thisAmount = 30000;
                if(performance.getAudience() > 20) {
                    thisAmount += (performance.getAudience() - 20) * 500 + 10000;
                }
                thisAmount += 300 * performance.getAudience();
            } else {
                throw new RuntimeException("알수없는 장르: " + play.getType());
            }

            volumeCredits += Math.max(performance.getAudience() - 30, 0);
            if(PlayType.COMEDY.equals(play.getType())) {
                volumeCredits += Math.floor(performance.getAudience() / 5);
            }

            printBill += play.getName() + ": $" + (thisAmount / 100)  + "(" + performance.getAudience() + "석)\n";
            totalAmount += thisAmount;
        }

        printBill += "총액: $" + (totalAmount / 100)+ "\n";
        printBill += "적립 포인트: " + + volumeCredits +"점\n";

        return printBill;
    }
}
