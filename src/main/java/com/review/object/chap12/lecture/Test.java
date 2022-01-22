package com.review.object.chap12.lecture;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        Lecture lecture = new GradeLecture("A", 10, new ArrayList<>(), new ArrayList<>());
        System.out.println(lecture.stat());
    }
}
