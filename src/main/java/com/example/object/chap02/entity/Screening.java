package com.example.object.chap02.entity;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class Screening {

    private Movie movie;

    private int sequence;

    private LocalDateTime whenScreened;

    public LocalDateTime getStartTime() {
        return this.whenScreened;
    }

    public boolean isSequence(int sequence) {
        return this.sequence == sequence;
    }

    public Money getMovieMoney() {
        return movie.getFee();
    }

    public Reservation reseve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }
}
