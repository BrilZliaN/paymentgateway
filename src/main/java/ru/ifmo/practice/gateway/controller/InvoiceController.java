package ru.ifmo.practice.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ifmo.practice.gateway.api.InvoiceApiDelegate;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.api.models.TransactionReadinessView;
import ru.ifmo.practice.gateway.assembler.InvoiceViewModelAssembler;
import ru.ifmo.practice.gateway.assembler.TransactionReadinessViewModelAssembler;
import ru.ifmo.practice.gateway.logic.bank.CreateInvoiceOperation;
import ru.ifmo.practice.gateway.logic.bank.CreateTransactionOperation;
import ru.ifmo.practice.gateway.logic.bank.GetTransactionOperation;

@Controller
@RequiredArgsConstructor
public class InvoiceController implements InvoiceApiDelegate {

    private final InvoiceViewModelAssembler invoiceViewModelAssembler;
    private final TransactionReadinessViewModelAssembler transactionReadinessViewModelAssembler;

    private final CreateInvoiceOperation createInvoiceOperation;
    private final GetTransactionOperation getTransactionOperation;
    private final CreateTransactionOperation createTransactionOperation;

    @PostMapping("/invoice")
    public ResponseEntity<InvoiceView> addInvoice(@RequestBody InvoicePostView invoicePostView) {
        var invoiceView = createInvoiceOperation.process(invoicePostView);
        if (invoiceView != null) {
            var model = invoiceViewModelAssembler.toModel(invoiceView);
            return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(invoiceView);
        } else  {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<ru.ifmo.practice.gateway.api.models.TransactionReadinessView> getInvoiceStatus(@PathVariable Long id) {
        var transaction = getTransactionOperation.process(id);
        if (transaction != null) {
            var view = new TransactionReadinessView();
            view.setType(TransactionReadinessView.TypeEnum.valueOf(transaction.getStatusCode()));
            var model = transactionReadinessViewModelAssembler.toModel(view);
            return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(view);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    @PostMapping("/invoice/{id}")
    public ResponseEntity<Void> createTransaction(@PathVariable Long id, @RequestBody ru.ifmo.practice.gateway.api.models.CreditCardView creditCardView) {
        if (createTransactionOperation.process(id, creditCardView)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

}
