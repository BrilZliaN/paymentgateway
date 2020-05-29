package ru.ifmo.practice.coordiantor.coordinator.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Invoice {

    private Long id;

    private LocalDateTime created;

    private double sum;
}
