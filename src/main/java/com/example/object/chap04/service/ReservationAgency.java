package com.example.object.chap04.service;

import com.example.object.chap02.entity.Money;
import com.example.object.chap04.entity.*;
import org.springframework.data.repository.query.Param;

public class ReservationAgency {

    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        Movie movie = screening.getMovie();
        boolean discountable = movie.getDiscountConditions().stream()
                .filter(condition -> condition.getType().equals(DiscountConditionType.PERIOD))
                .map(condition -> {
                    return screening.getWhenScreened().getDayOfWeek().equals(condition.getDayOfWeek()) &&
                            condition.getStartTime().compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
                            condition.getEndTime().compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
                }).findFirst().get() ? true :
                movie.getDiscountConditions().stream()
                        .filter(condition -> condition.getType().equals(DiscountConditionType.SEQUENCE))
                        .map(condition -> condition.getSequence() == screening.getSequence()).findFirst().get();

        Money fee = movie.getFee();
        if(discountable) {
            Money discountAmount = Money.ZERO;
            switch (movie.getMovieType()) {
                case AMOUNT_DISCOUNT:
                    discountAmount = movie.getDiscountAmount();
                    break;
                case PERCENT_DISCOUNT:
                    discountAmount = movie.getFee().times(movie.getDiscountPercent());
                    break;
                case NONE_DISCOUNT:
                    discountAmount = Money.ZERO;
                    break;
            }
            fee.minus(discountAmount);
        }

        return new Reservation(customer, screening, fee, audienceCount);
    }
}
