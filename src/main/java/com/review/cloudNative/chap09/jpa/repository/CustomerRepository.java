package com.review.cloudNative.chap09.jpa.repository;

import com.review.cloudNative.chap09.jpa.entity.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Optional<Customer> findByEmailContaining(String email);
}
