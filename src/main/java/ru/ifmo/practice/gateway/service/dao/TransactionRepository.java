package ru.ifmo.practice.gateway.service.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.ifmo.practice.gateway.dto.entity.Transaction;

import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.invoice.id = :invoiceId")
    Optional<Transaction> findByInvoiceId(@Param("invoiceId") Long invoiceId);
}
