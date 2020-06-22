package ru.ifmo.practice.gateway.builder;

import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.dto.entity.Card;
import ru.ifmo.practice.gateway.dto.entity.Invoice;
import ru.ifmo.practice.gateway.dto.entity.Transaction;

import java.time.LocalDateTime;

@Component
public class TransactionBuilder {

    public Transaction build(Card card, Invoice invoice) {
        var transaction = new Transaction();
        transaction.setCard(card);
        transaction.setInvoice(invoice);
        transaction.setStatusDate(LocalDateTime.now());
        return transaction;
    }

    public void update(Transaction transaction, TransactionStatusView statusView) {
        transaction.setStatusDate(LocalDateTime.now());
        transaction.setStatusCode(statusView.getAnswerCode());
    }
}
