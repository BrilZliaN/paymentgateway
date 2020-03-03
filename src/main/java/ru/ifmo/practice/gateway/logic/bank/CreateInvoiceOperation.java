package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.InvoiceDaoAdapter;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;

@Component
@RequiredArgsConstructor
public class CreateInvoiceOperation {

    private final InvoiceDaoAdapter invoiceDaoAdapter;

    public InvoiceView process(InvoicePostView invoiceView) {
        try {
            return invoiceDaoAdapter.addInvoice(invoiceView);
        } catch (PaymentGatewayException e) {
            return null;
        }
    }

}
