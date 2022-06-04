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
public class Invoice extends BaseEntity {

    @Id
    private String invoiceId;

    private String customerId;

    private List<Order> orders = new ArrayList<>();

    private Address billingAddress;

    private InvoiceStatus invoiceStatus;

    public Invoice(String customerId, List<Order> orders, Address billingAddress, InvoiceStatus invoiceStatus) {
        this.customerId = customerId;
        this.orders = orders;
        this.billingAddress = billingAddress;
        this.invoiceStatus = invoiceStatus;
    }

    public void addOrder(Order order) {
        order.setAccountNumber(customerId);
        this.orders.add(order);
    }
}
