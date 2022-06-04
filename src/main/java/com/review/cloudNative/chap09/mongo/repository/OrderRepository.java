package com.review.cloudNative.chap09.mongo.repository;


import com.review.cloudNative.chap09.mongo.doc.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, String> {
}
