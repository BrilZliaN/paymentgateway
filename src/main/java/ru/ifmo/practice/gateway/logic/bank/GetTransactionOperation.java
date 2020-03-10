package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.dto.entity.Transaction;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Component
@RequiredArgsConstructor
public class GetTransactionOperation {

    private final TransactionDaoAdapter transactionDaoAdapter;

    public Transaction process(long id) {
        return transactionDaoAdapter.getTransactionById(id);
    }

}
