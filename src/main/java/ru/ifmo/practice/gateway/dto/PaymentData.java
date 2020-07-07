package ru.ifmo.practice.gateway.dto;

import lombok.Getter;
import lombok.Setter;
import ru.ifmo.practice.gateway.dto.entity.Transaction;

@Getter
@Setter
public class PaymentData {
    public PaymentData(Transaction transaction, String source) {
        this.transaction = transaction;
        this.source = source;
    }

    public Transaction transaction;
    public String source;
}