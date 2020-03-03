package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Component
@RequiredArgsConstructor
public class UpdateTransactionStatusOperation {

    private final TransactionDaoAdapter daoAdapter;

    public boolean process(long transactionId, TransactionStatusView status) {
        try {
            daoAdapter.updateTransactionStatus(transactionId, status);
            return true;
        } catch (PaymentGatewayException e) {
            return false;
        }
    }
}
