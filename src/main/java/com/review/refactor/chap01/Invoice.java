package com.review.refactor.chap01;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Invoice {

    private String customer;

    private List<Performance> performances;

    private List<Order> orders;

    private LocalDateTime dueDate;

    public Invoice() {
        performances = new ArrayList<>();
    }

    public void addPerformance(Performance performance) {
        performances.add(performance);
    }
}
