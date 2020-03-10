package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Component
@RequiredArgsConstructor
public class UpdateTransactionStatusOperation {

    private final TransactionDaoAdapter daoAdapter;

    public void process(long transactionId, TransactionStatusView status) {
        daoAdapter.updateTransactionStatus(transactionId, status);
    }
}
