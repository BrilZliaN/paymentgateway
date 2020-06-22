package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.helper.InvoicePostViewValidator;
import ru.ifmo.practice.gateway.service.dao.InvoiceDaoAdapter;

@Component
@RequiredArgsConstructor
public class CreateInvoiceOperation {

    private final InvoiceDaoAdapter invoiceDaoAdapter;

    private final InvoicePostViewValidator invoicePostViewValidator;

    public InvoiceView process(InvoicePostView invoiceView) {
        invoicePostViewValidator.validate(invoiceView);
        return invoiceDaoAdapter.addInvoice(invoiceView);
    }

}
