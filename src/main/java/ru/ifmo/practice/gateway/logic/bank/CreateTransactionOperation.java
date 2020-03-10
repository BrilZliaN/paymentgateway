package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Component
@RequiredArgsConstructor
public class CreateTransactionOperation {

    private final TransactionDaoAdapter transactionDaoAdapter;

    public void process(Long invoiceId, CreditCardView creditCardView) {
        transactionDaoAdapter.createTransaction(invoiceId, creditCardView);
    }

}
