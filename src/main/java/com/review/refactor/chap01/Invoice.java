package com.review.refactor.chap01;

import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private String customer;

    private List<Performance> performances;

    public Invoice() {
        performances = new ArrayList<>();
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    public void addPerformance(Performance performance) {
        performances.add(performance);
    }
}
