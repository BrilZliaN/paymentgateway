package ru.ifmo.practice.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.ifmo.practice.gateway.api.InvoiceApiDelegate;
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.api.models.TransactionReadinessView;
import ru.ifmo.practice.gateway.logic.bank.CreateInvoiceOperation;
import ru.ifmo.practice.gateway.logic.bank.CreateTransactionOperation;
import ru.ifmo.practice.gateway.logic.bank.GetTransactionOperation;

@Controller
@RequiredArgsConstructor
public class InvoiceController implements InvoiceApiDelegate {

    private final CreateInvoiceOperation createInvoiceOperation;
    private final GetTransactionOperation getTransactionOperation;
    private final CreateTransactionOperation createTransactionOperation;

    public ResponseEntity<InvoiceView> addInvoice(InvoicePostView invoicePostView) {
        var invoiceView = createInvoiceOperation.process(invoicePostView);
        return ResponseEntity.ok(invoiceView);
    }

    public ResponseEntity<TransactionReadinessView> getInvoiceStatus(Long id) {
        var transaction = getTransactionOperation.process(id);
        var view = new TransactionReadinessView();
        view.setType(TransactionReadinessView.TypeEnum.valueOf(transaction.getStatusCode()));
        return ResponseEntity.ok(view);
    }

    public ResponseEntity<Void> createTransaction(Long id, CreditCardView creditCardView) {
        createTransactionOperation.process(id, creditCardView);
        return ResponseEntity.ok().build();
    }

}