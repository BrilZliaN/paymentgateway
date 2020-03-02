package ru.ifmo.practice.gateway.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.builder.CardBuilder;
import ru.ifmo.practice.gateway.builder.TransactionBuilder;
import ru.ifmo.practice.gateway.dto.entity.Card;
import ru.ifmo.practice.gateway.dto.entity.Transaction;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class TransactionDaoAdapter {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final InvoiceDaoAdapter invoiceDaoAdapter;
    private final TransactionBuilder transactionBuilder;
    private final CardBuilder cardBuilder;

    @Transactional
    public void createTransaction(Long invoiceId, CreditCardView creditCardView) {
        final var invoice = invoiceDaoAdapter.getInvoiceById(invoiceId);
        final var card = addCard(creditCardView);
        final var transaction = transactionBuilder.build(card, invoice);
        try {
            transactionRepository.save(transaction);
        } catch (DataAccessException dataAccessException) {
            throw ExceptionFactory.wrap(dataAccessException, "Невозможно создать транзакцию");
        }
    }

    public void updateTransactionStatus(Long id, TransactionStatusView transactionStatusView) {
        final var transaction = getTransactionById(id);
        transactionBuilder.update(transaction, transactionStatusView);
        try {
            transactionRepository.save(transaction);
        } catch (DataAccessException dataAccessException) {
            throw ExceptionFactory.wrap(dataAccessException, "Невозможно обновить статус транзакции");
        }
    }

    public Transaction getTransactionById(Long id) {
        try {
            return transactionRepository.findById(id).orElseThrow(ExceptionFactory::notFound);
        } catch (DataAccessException dataAccessException) {
            throw ExceptionFactory.wrap(dataAccessException);
        }
    }

    @Transactional(value = Transactional.TxType.SUPPORTS)
    Card addCard(CreditCardView creditCardView) {
        final var card = cardBuilder.build(creditCardView);
        try {
            return cardRepository.save(card);
        } catch (DataAccessException dataAccessException) {
            throw ExceptionFactory.wrap(dataAccessException);
        }
    }
}
