package com.review.cloudNative.chap09.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Data
@NoArgsConstructor @AllArgsConstructor
public class CreditCard extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String number;

    @Enumerated(value = EnumType.STRING)
    private CreditCardType creditCardType;

    public CreditCard(String number, CreditCardType creditCardType) {
        this.number = number; this.creditCardType = creditCardType;
    }
}
