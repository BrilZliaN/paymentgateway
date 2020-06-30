package ru.ifmo.practice.gateway.builder;

import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.TransactionView;
import ru.ifmo.practice.gateway.api.models.TransactionsList;
import ru.ifmo.practice.gateway.dto.entity.Transaction;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TransactionsListBuilder {

    public TransactionsList build(List<Transaction> transactions) {
        TransactionsList result = new TransactionsList();
        transactions.stream().map(this::buildTransactionView).forEach(result::add);
        return result;
    }

    private TransactionView buildTransactionView(Transaction transaction) {
        var result = new TransactionView();
        result.setId(transaction.getId());
        result.setCreated(transaction.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        result.setStatus(TransactionView.StatusEnum.valueOf(transaction.getStatusCode()));
        return result;
    }

}
