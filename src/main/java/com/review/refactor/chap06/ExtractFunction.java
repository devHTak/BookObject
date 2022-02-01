package com.review.refactor.chap06;

import com.review.refactor.chap01.Invoice;
import com.review.refactor.chap01.Order;
import com.review.refactor.chap01.Performance;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 함수 추출하기 예시
 */
@Slf4j
public class ExtractFunction {

    public void printOwingBeforeRefactoring(Invoice invoice) {
        int outstanding = 0;

        log.info("=============");
        log.info("===고객 채무===");
        log.info("=============");

        // 미해결 채무 계산
        for(Order order: invoice.getOrders()) {
            outstanding += order.getAmount();
        }

        // 마감일 기록
        LocalDateTime dueDate = LocalDateTime.now();
        dueDate.plusDays(30);
        invoice.setDueDate(dueDate);

        // 세부 사항 출력
        log.info("고객명: {}", invoice.getCustomer());
        log.info("채무액: {}", outstanding);
        log.info("마감일: {}", invoice.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public void printOwing(Invoice invoice) {
        printBanner();
        int outstanding = calculateOutstanding(invoice);
        recordDueDate(invoice);
        printLog(invoice, outstanding);
    }

    private void printLog(Invoice invoice, int outstanding) {
        log.info("고객명: {}", invoice.getCustomer());
        log.info("채무액: {}", outstanding);
        log.info("마감일: {}", invoice.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    private void recordDueDate(Invoice invoice) {
        LocalDateTime dueDate = LocalDateTime.now();
        dueDate.plusDays(30);
        invoice.setDueDate(dueDate);
    }

    private int calculateOutstanding(Invoice invoice) {
        return invoice.getOrders().stream()
                .map(order -> order.getAmount())
                .reduce(0, (sum, now) -> sum += now);
    }

    private void printBanner() {
        log.info("=============");
        log.info("===고객 채무===");
        log.info("=============");
    }
}
