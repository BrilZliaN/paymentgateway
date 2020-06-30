package ru.ifmo.practice.coordiantor.coordinator.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {

    private Long id;

    private LocalDateTime created;

    private String statusCode;

    private LocalDateTime statusDate;

    @ManyToOne
    private Card card;

    @ManyToOne
    private Invoice invoice;
}
