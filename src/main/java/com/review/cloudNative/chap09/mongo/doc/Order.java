package com.review.cloudNative.chap09.mongo.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data @NoArgsConstructor @AllArgsConstructor
public class Order extends BaseEntity {

    @Id
    private String orderId;

    private String accountNumber;

    private OrderStatus orderStatus;

    private List<LineItem> lineItems = new ArrayList<>();

    private Address shippingAddress;

    public Order(String accountNumber, OrderStatus orderStatus, List<LineItem> lineItems, Address shippingAddress) {
        this.accountNumber = accountNumber;
        this.orderStatus = orderStatus;
        this.lineItems = lineItems;
        this.shippingAddress = shippingAddress;
    }

    public void addLineItem(LineItem lineItem) {
        this.lineItems.add(lineItem);
    }
}
