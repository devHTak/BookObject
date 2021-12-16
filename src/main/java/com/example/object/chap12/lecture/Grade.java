package com.example.object.chap12.lecture;

public class Grade {
    private String name;
    private int upper;
    private int lower;

    public Grade(String name, int upper, int lower) {
        this.name = name; this.upper = upper; this.lower = lower;
    }

    public String getName() {
        return this.name;
    }

    public boolean isName(String name) {
        return this.name.equals(name);
    }

    public boolean include(int score) {
        return score >= this.lower && score <= upper;
    }
}
