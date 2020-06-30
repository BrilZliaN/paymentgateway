package ru.ifmo.practice.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ifmo.practice.gateway.api.TransactionApiDelegate;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.logic.bank.UpdateTransactionStatusOperation;

@Controller
@RequiredArgsConstructor
public class TransactionController implements TransactionApiDelegate {

    private final UpdateTransactionStatusOperation updateTransactionStatusOperation;

    @PutMapping("/transaction/{transactionId}")
    public ResponseEntity<Void> updateTransactionStatus(@PathVariable Long transactionId, @RequestBody TransactionStatusView transactionStatus) {
        updateTransactionStatusOperation.process(transactionId, transactionStatus);
        return ResponseEntity.ok().build();
    }

}