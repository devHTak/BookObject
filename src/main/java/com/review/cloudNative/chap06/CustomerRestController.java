package com.review.cloudNative.chap06;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/v1/customers")
public class CustomerRestController {

    @Autowired private CustomerRepository customerRepository;

    @RequestMapping(method = RequestMethod.OPTIONS)
    ResponseEntity<?> options() {
        return ResponseEntity.ok().allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.HEAD, HttpMethod.OPTIONS,
                HttpMethod.PUT, HttpMethod.DELETE).build();
    }

    @GetMapping
    ResponseEntity<Collection<Customer>> getCollection() {
        return ResponseEntity.ok(customerRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Customer> get(@PathVariable Long id) {
        return customerRepository.findById(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new CustomerNotFoundException());
    }

    @PostMapping
    ResponseEntity<Customer> post(@RequestBody Customer customer) {
        Customer returnCustomer = customerRepository.save(customer);
        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}")
                .buildAndExpand(returnCustomer.getId())
                .toUri();

        return ResponseEntity.created(uri).body(customer);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(c -> {
                    customerRepository.delete(c);
                    return ResponseEntity.noContent().build();
                }).orElseThrow(() -> new CustomerNotFoundException());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    ResponseEntity<?> head(@PathVariable Long id) {
        return this.customerRepository.findById(id)
                .map(exists -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new CustomerNotFoundException());
    }

    @PutMapping(value="/{id}")
    ResponseEntity<Customer> put(@PathVariable Long id, @RequestBody Customer customer) {
        return customerRepository.findById(id)
                .map(exists -> {
                    Customer returnCustomer = customerRepository.save(
                            new Customer(exists.getId(), customer.getFirstName(), customer.getLastName()));
                    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
                    return ResponseEntity.created(uri).body(returnCustomer);
                }).orElseThrow(() -> new CustomerNotFoundException());
    }

}
