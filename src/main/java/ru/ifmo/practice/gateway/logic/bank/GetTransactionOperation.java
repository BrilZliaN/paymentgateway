package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.dto.entity.Transaction;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Component
@RequiredArgsConstructor
public class GetTransactionOperation {

    private final TransactionDaoAdapter transactionDaoAdapter;

    public Transaction process(long id) {
        try {
            return transactionDaoAdapter.getTransactionById(id);
        } catch (PaymentGatewayException e) {
            return null;
        }
    }

}
