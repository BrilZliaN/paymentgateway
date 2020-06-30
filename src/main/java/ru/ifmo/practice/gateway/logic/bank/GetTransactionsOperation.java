package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.TransactionsList;
import ru.ifmo.practice.gateway.builder.TransactionsListBuilder;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Component
@RequiredArgsConstructor
public class GetTransactionsOperation {

    private TransactionDaoAdapter transactionDaoAdapter;

    private TransactionsListBuilder transactionsListBuilder;

    public TransactionsList process() {
        return transactionsListBuilder.build(transactionDaoAdapter.getAllTransactions());
    }

}
