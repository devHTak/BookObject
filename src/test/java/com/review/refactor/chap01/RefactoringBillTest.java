package com.review.refactor.chap01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RefactoringBillTest {

    @Autowired
    private RefactoringBill bill;

    @Test
    @DisplayName("function bill 테스트")
    void statementTest() throws Exception {
        String testResult =
                "청구 내역 (고객명: BigCo)\n" +
                "Hamlet: $650(55석)\n" +
                "As You Like It: $580(35석)\n" +
                "Othello: $500(40석)\n" +
                "총액: $1730\n" +
                "적립 포인트: 47점";
        Play hamlet = new Play("hamelet", "Hamlet", PlayType.TRAGEDY);
        Play asLike = new Play("as-like", "As You Like It", PlayType.COMEDY);
        Play othello = new Play("othello", "Othello", PlayType.TRAGEDY);

        Performance hamletPerformance = new Performance("hamlet", 55);
        hamletPerformance.setPerformanceCalculator(hamlet);
        Performance asLikePerformance = new Performance("as-like", 35);
        asLikePerformance.setPerformanceCalculator(asLike);
        Performance othelloPerformance = new Performance("othello", 40);
        othelloPerformance.setPerformanceCalculator(othello);

        Invoice invoice = new Invoice();
        invoice.setCustomer("BigCo");
        invoice.addPerformance(hamletPerformance);
        invoice.addPerformance(asLikePerformance);
        invoice.addPerformance(othelloPerformance);

        Map<String, Play> playMap = new HashMap<>();
        playMap.put(hamletPerformance.getPlayId(), hamlet);
        playMap.put(asLikePerformance.getPlayId(), asLike);
        playMap.put(othelloPerformance.getPlayId(), othello);

        String result = bill.statement(invoice, playMap);
        assertThat(testResult).isEqualToIgnoringWhitespace(result);
    }

}