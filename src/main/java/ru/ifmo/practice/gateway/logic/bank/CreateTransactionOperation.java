package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.api.models.TransactionReadinessView;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.dto.entity.Transaction;
import ru.ifmo.practice.gateway.helper.CreditCardValidator;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Component
@RequiredArgsConstructor
public class CreateTransactionOperation {

    private final TransactionDaoAdapter transactionDaoAdapter;

    public Transaction process(Long invoiceId, CreditCardView creditCardView) {
        CreditCardValidator.validate(creditCardView);
        var transaction = transactionDaoAdapter.createTransaction(invoiceId, creditCardView);
        transactionDaoAdapter.updateTransactionStatus(transaction.getId(),
                new TransactionStatusView().answerCode(TransactionReadinessView.TypeEnum.PROCESSING.toString()));
        return transactionDaoAdapter.getTransactionById(transaction.getId());
    }

}
