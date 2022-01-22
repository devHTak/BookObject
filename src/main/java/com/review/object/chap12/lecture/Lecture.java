package com.review.object.chap12.lecture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lecture {

    private int pass;
    private String title;
    private List<Integer> scores = new ArrayList<>();

    public Lecture(String title, int pass, List<Integer> scores) {
        this.title = title; this.pass = pass; this.scores = scores;
    }

    public double average() {
        return scores.stream().mapToInt(Integer::intValue)
                .average().orElse(0);
    }

    public List<Integer> getScores() {
        return Collections.unmodifiableList(this.scores);
    }

    public String evaluate() {
        return String.format("Pass: %d, Fail: %d", passCount(), failCount());
    }

    private long failCount() {
        return scores.stream().filter(score -> score < pass).count();
    }

    private long passCount() {
        return scores.stream().filter(score -> score >= pass).count();
    }

    public String stat() {
        return String.format("Title: %s, Evaluation Method: %s", title, getEvaluationMethod());
    }

    protected String getEvaluationMethod() {
        return "LECTURE";
    }

}
