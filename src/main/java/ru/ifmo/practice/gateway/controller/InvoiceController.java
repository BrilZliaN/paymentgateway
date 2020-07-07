package ru.ifmo.practice.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ifmo.practice.gateway.api.InvoiceApiDelegate;
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.api.models.TransactionReadinessView;
import ru.ifmo.practice.gateway.logic.bank.CreateInvoiceOperation;
import ru.ifmo.practice.gateway.logic.bank.CreateTransactionOperation;
import ru.ifmo.practice.gateway.logic.bank.GetTransactionOperation;
import ru.ifmo.practice.gateway.logic.bank.RequestPaymentOperation;

@Controller
@RequiredArgsConstructor
public class InvoiceController implements InvoiceApiDelegate {

    private final CreateInvoiceOperation createInvoiceOperation;
    private final GetTransactionOperation getTransactionOperation;
    private final CreateTransactionOperation createTransactionOperation;
    private final RequestPaymentOperation requestPaymentOperation;

    public ResponseEntity<InvoiceView> addInvoice(@RequestBody InvoicePostView invoicePostView) {
        var invoiceView = createInvoiceOperation.process(invoicePostView);
        return new ResponseEntity<>(invoiceView, HttpStatus.CREATED);
    }

    public ResponseEntity<TransactionReadinessView> getInvoiceStatus(@PathVariable Long invoiceId) {
        var transaction = getTransactionOperation.process(invoiceId);
        var view = new TransactionReadinessView();
        view.setType(TransactionReadinessView.TypeEnum.valueOf(transaction.getStatusCode()));
        return ResponseEntity.ok(view);
    }

    public ResponseEntity<Void> createTransaction(@PathVariable Long invoiceId, @RequestBody CreditCardView creditCard) {
        var transaction = createTransactionOperation.process(invoiceId, creditCard);
        requestPaymentOperation.process(transaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}