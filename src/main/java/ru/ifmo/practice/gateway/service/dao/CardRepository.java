package ru.ifmo.practice.gateway.service.dao;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.practice.gateway.dto.entity.Card;

public interface CardRepository extends CrudRepository<Card, Long> {
}
