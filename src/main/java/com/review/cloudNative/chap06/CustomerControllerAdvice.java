package com.review.cloudNative.chap06;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@ControllerAdvice(annotations = RestController.class)
public class CustomerControllerAdvice {
    private final MediaType vndErrorMediaType = MediaType.parseMediaType("application/vnd.error");

    @ExceptionHandler(CustomerNotFoundException.class)
    ResponseEntity<VndErrors> notFoundException(CustomerNotFoundException e) {
        return this.error(e, HttpStatus.NOT_FOUND, e.getCustomerId() + "");
    }

    private ResponseEntity<VndErrors> error(RuntimeException e, HttpStatus httpStatus, String logref) {
        String msg = Optional.of(e.getMessage()).orElse(e.getClass().getSimpleName());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(this.vndErrorMediaType);

        return new ResponseEntity<>(new VndErrors(logref, msg), httpHeaders, httpStatus);
    }
}
