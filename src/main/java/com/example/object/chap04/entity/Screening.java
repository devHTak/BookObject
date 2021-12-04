package com.example.object.chap04.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Screening {

    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;
}
