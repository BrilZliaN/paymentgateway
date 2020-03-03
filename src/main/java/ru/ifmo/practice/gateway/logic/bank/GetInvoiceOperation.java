package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.builder.InvoiceViewBuilder;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.InvoiceDaoAdapter;

@Component
@RequiredArgsConstructor
public class GetInvoiceOperation {

    private final InvoiceDaoAdapter invoiceDaoAdapter;

    public InvoiceView process(long id) {
        try {
            return new InvoiceViewBuilder().build(invoiceDaoAdapter.getInvoiceById(id));
        } catch (PaymentGatewayException e) {
            return null;
        }
    }

}
