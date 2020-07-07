package ru.ifmo.practice.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ifmo.practice.gateway.api.TransactionApiDelegate;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.api.models.TransactionsList;
import ru.ifmo.practice.gateway.logic.bank.GetTransactionsOperation;
import ru.ifmo.practice.gateway.logic.bank.UpdateTransactionStatusOperation;

@Controller
@RequiredArgsConstructor
public class TransactionController implements TransactionApiDelegate {

    private final UpdateTransactionStatusOperation updateTransactionStatusOperation;

    private final GetTransactionsOperation getTransactionsOperation;

    public ResponseEntity<Void> updateTransactionStatus(@PathVariable Long transactionId, @RequestBody TransactionStatusView transactionStatus) {
        updateTransactionStatusOperation.process(transactionId, transactionStatus);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<TransactionsList> getTransactions() {
        var result = getTransactionsOperation.process();
        return ResponseEntity.ok(result);
    }

}