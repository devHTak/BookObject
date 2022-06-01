package com.review.cloudNative.chap06;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Customer {
    @Id @GeneratedValue
    private Long id;

    private String lastName;

    private String firstName;
}
