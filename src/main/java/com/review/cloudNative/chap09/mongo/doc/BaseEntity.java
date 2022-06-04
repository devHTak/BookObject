package com.review.cloudNative.chap09.mongo.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class BaseEntity {

    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

}
