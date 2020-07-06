package ru.ifmo.practice.coordiantor.coordinator.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Transaction {

    private Long id;

    private LocalDateTime created;

    private String statusCode;

    private LocalDateTime statusDate;

    private Card card;

    private Invoice invoice;
}
