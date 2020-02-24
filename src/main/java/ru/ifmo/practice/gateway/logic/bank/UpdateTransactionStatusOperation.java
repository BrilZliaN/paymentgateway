package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;

@Component
@RequiredArgsConstructor
public class UpdateTransactionStatusOperation {

    // private final TransactionDaoAdapter daoAdapter;

    public boolean process(long transactionId, TransactionStatusView status) {
        // return daoAdapter.updateTransactionStatus(transactionId, status.getAnswerCode());
        return true;
    }
}
