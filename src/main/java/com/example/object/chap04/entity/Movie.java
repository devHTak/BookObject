package com.example.object.chap04.entity;

import com.example.object.chap02.entity.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Movie {

    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    /**
     * MovieType에 따라 discountAmount, discountPercent 에 데이터가 입력된다
     */
    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;

}
