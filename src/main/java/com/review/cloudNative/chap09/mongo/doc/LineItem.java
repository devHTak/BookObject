package com.review.cloudNative.chap09.mongo.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class LineItem extends BaseEntity {
    private String name;
    private String productId;
    private Integer quantity;
    private Double price;
    private Double tax;
}
