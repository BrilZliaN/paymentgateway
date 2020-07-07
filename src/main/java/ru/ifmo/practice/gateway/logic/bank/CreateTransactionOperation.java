package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.dto.entity.Transaction;
import ru.ifmo.practice.gateway.helper.CreditCardValidator;
import ru.ifmo.practice.gateway.helper.IdValidator;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Component
@RequiredArgsConstructor
public class CreateTransactionOperation {

    private final CreditCardValidator creditCardValidator;
    private final TransactionDaoAdapter transactionDaoAdapter;
    private final IdValidator idValidator;

    public Transaction process(Long invoiceId, CreditCardView creditCardView) {
        idValidator.validate(invoiceId);
        creditCardValidator.validate(creditCardView);
        return transactionDaoAdapter.createTransaction(invoiceId, creditCardView);
    }

}
