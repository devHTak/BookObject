package com.review.cloudNative.chap09.jpa.repository;

import com.review.cloudNative.chap09.jpa.entity.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
}
