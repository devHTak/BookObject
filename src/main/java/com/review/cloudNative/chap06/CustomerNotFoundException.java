package com.review.cloudNative.chap06;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerNotFoundException extends RuntimeException{

    private Long customerId;
}
