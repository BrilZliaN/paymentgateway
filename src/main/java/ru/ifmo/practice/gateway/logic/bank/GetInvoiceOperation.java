package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.builder.InvoiceViewBuilder;
import ru.ifmo.practice.gateway.helper.IdValidator;
import ru.ifmo.practice.gateway.service.dao.InvoiceDaoAdapter;

@Component
@RequiredArgsConstructor
public class GetInvoiceOperation {

    private final InvoiceDaoAdapter invoiceDaoAdapter;

    private final InvoiceViewBuilder invoiceViewBuilder;

    private final IdValidator idValidator;

    public InvoiceView process(long id) {
        idValidator.validate(id);
        var invoice = invoiceDaoAdapter.getInvoiceById(id);
        return invoiceViewBuilder.build(invoice);
    }

}
