package com.review.cloudNative.chap09.mongo.repository;

import com.review.cloudNative.chap09.mongo.doc.Address;
import com.review.cloudNative.chap09.mongo.doc.Invoice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, String> {

    Invoice findByBillingAddress(Address address);
}
