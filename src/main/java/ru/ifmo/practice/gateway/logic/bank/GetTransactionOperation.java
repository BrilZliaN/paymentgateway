package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.dto.entity.Transaction;
import ru.ifmo.practice.gateway.helper.IdValidator;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Component
@RequiredArgsConstructor
public class GetTransactionOperation {

    private final TransactionDaoAdapter transactionDaoAdapter;

    private final IdValidator idValidator;

    public Transaction process(long id) {
        idValidator.validate(id);
        return transactionDaoAdapter.getTransactionByInvoiceId(id);
    }

}
