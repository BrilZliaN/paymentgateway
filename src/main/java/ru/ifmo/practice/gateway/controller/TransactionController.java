package ru.ifmo.practice.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.ifmo.practice.gateway.api.TransactionApiDelegate;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.logic.bank.UpdateTransactionStatusOperation;

@Controller
@RequiredArgsConstructor
public class TransactionController implements TransactionApiDelegate {

    private final UpdateTransactionStatusOperation updateTransactionStatusOperation;

    public ResponseEntity<Void> updateTransactionStatus(Long transactionId, TransactionStatusView transactionStatus) {
        updateTransactionStatusOperation.process(transactionId, transactionStatus);
        return ResponseEntity.ok().build();
    }

}