package com.review.cloudNative.chap09.mongo.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Address {
    private String street1;
    private String street2;
    private String state;
    private String city;
    private String country;
    private Integer zipCd;
}
