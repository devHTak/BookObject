package com.example.object.chap04.entity;

import com.example.object.chap02.entity.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Reservation {

    private Customer customer;

    private Screening screening;

    private Money fee;

    private int audienceCount;

}
