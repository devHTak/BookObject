package com.review.object.chap12.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GradeLecture extends Lecture {

    private List<Grade> grades = new ArrayList<>();

    public GradeLecture(String title, int pass, List<Integer> scores, List<Grade> grades) {
        super(title, pass, scores);
        this.grades = grades;
    }

    @Override
    public String evaluate() {
        return super.evaluate() + ", " + gradesStatistics();
    }

    public double average(String gradeName) {
        return grades.stream().filter(grade -> grade.isName(gradeName))
                .findFirst()
                .map(this::gradeAverage).orElse(0d);
    }

    private double gradeAverage(Grade grade) {
        return getScores().stream()
                .filter(grade::include)
                .mapToInt(Integer::intValue)
                .average().orElse(0);
    }

    private String gradesStatistics() {
        return grades.stream().map(grade -> format(grade)).collect(Collectors.joining(" "));
    }

    private String format(Grade grade) {
        return String.format("%s: %d", grade.getName(), gradeCount(grade));
    }

    public long gradeCount(Grade grade) {
        return getScores().stream().filter(grade::include).count();
    }

    @Override
    protected String getEvaluationMethod() {
        return "GRADE LECTURE";
    }
}
