package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.service.dao.InvoiceDaoAdapter;

@Component
@RequiredArgsConstructor
public class CreateInvoiceOperation {

    private final InvoiceDaoAdapter invoiceDaoAdapter;

    public InvoiceView process(InvoicePostView invoiceView) {
        return invoiceDaoAdapter.addInvoice(invoiceView);
    }

}
