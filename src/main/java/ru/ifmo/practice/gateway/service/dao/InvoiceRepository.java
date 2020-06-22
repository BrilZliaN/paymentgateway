package ru.ifmo.practice.gateway.service.dao;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.practice.gateway.dto.entity.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

}
