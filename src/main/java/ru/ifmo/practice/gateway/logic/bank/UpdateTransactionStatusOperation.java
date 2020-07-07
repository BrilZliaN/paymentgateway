package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.helper.IdValidator;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Component
@RequiredArgsConstructor
public class UpdateTransactionStatusOperation {

    private final TransactionDaoAdapter daoAdapter;
    private final IdValidator idValidator;

    public void process(long transactionId, TransactionStatusView status) {
        idValidator.validate(transactionId);
        daoAdapter.updateTransactionStatus(transactionId, status);
    }
}
