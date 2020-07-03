package ru.ifmo.practice.coordiantor.coordinator.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Invoice {

    private Long id;

    private LocalDateTime created;

    private double sum;
}
