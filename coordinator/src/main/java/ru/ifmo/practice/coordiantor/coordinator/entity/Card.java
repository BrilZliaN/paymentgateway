package ru.ifmo.practice.coordiantor.coordinator.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Card {

    private Long id;

    private LocalDateTime created;

    private Long number;

    private String valid;

    private String owner;

}
