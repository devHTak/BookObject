package com.review.object.chap04.entity;

import com.review.object.chap02.entity.Money;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
public class Screening {

    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    /**
     * 낮은 응집도
     * DiscountPolicy -> Movie -> Screening 의존하는 관계로, DiscountPolicy에 변경, 추가등이 발생하면 3 객체 모두 수정해주어야 한다.
     */
    public Money calculateFee(int audienceCount) {
        switch (movie.getMovieType()) {
            case AMOUNT_DISCOUNT:
                if(movie.isDiscountable(whenScreened, sequence)) {
                    return movie.calculateAmountDiscountFee().times(audienceCount);
                }
                break;
            case NONE_DISCOUNT:
                return movie.calcluateNonDiscountFee().times(audienceCount);
            case PERCENT_DISCOUNT:
                if(movie.isDiscountable(whenScreened, sequence)) {
                    return movie.calculatePercentDiscountFee().times(audienceCount);
                }
                break;
        }
        return movie.calcluateNonDiscountFee().times(audienceCount);
    }
}
