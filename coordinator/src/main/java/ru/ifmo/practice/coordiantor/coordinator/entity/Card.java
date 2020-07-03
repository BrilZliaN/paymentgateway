package ru.ifmo.practice.coordiantor.coordinator.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class Card {

    private Long id;

    private LocalDateTime created;

    private Long number;

    private String valid;

    private String owner;

}
