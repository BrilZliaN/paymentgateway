package ru.ifmo.practice.gateway.builder;

import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;
import ru.ifmo.practice.gateway.dto.entity.Invoice;

@Component
public class InvoiceBuilder {

    public Invoice build(InvoicePostView invoicePostView) {
        var invoice = new Invoice();
        invoice.setSum(invoicePostView.getSum());
        return invoice;
    }
}
