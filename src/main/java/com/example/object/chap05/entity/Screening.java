package com.example.object.chap05.entity;

import com.example.object.chap02.entity.Money;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Screning
 * 영화 예매할 책임을 맡으며 그 결과로 Reservation을 리턴한다.
 * 즉, Reservation 창조자다.
 */
@NoArgsConstructor @AllArgsConstructor
public class Screening {

    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public LocalDateTime getWhenScreened() {
        return this.whenScreened;
    }

    public int getSequence(){
        return this.sequence;
    }

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    /**
     * 영화를 예매하기 위해서는 가격을 계산해야 한다.
     * Movie에게 가격 계산을 위임한다.
     * calculateMovieFee 에 Screening 객체를 넘김으로 써 Screening 에서 Movie 내부 로직은 알 수 없다.
     */
    public Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }
}
