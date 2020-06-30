package ru.ifmo.practice.gateway.dto.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", insertable = false, updatable = false)
    private LocalDateTime created;

    private double sum;
}
