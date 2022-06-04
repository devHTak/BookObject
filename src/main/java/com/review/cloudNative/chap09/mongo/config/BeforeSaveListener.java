package com.review.cloudNative.chap09.mongo.config;

import com.review.cloudNative.chap09.mongo.doc.BaseEntity;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BeforeSaveListener extends AbstractMongoEventListener<BaseEntity> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<BaseEntity> event) {
        LocalDateTime dateTime = LocalDateTime.now();
        if(event.getSource().getCreatedAt() == null) {
            event.getSource().setCreatedAt(dateTime);
        }

        event.getSource().setLastModified(dateTime);
        super.onBeforeSave(event);
    }
}
