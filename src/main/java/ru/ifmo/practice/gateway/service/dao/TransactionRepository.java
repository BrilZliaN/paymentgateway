package ru.ifmo.practice.gateway.service.dao;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.practice.gateway.dto.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
