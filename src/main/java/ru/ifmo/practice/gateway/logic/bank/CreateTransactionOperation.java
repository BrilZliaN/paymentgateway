package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;
import ru.ifmo.practice.gateway.api.models.CreditCardView;

@Component
@RequiredArgsConstructor
public class CreateTransactionOperation {

    private final TransactionDaoAdapter transactionDaoAdapter;

    public boolean process(Long invoiceId, CreditCardView creditCardView) {
        try {
            transactionDaoAdapter.createTransaction(invoiceId, creditCardView);
            return true;
        } catch (PaymentGatewayException e) {
            return false;
        }
    }

}
