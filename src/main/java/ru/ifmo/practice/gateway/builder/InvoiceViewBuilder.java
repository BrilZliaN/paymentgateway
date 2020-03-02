package ru.ifmo.practice.gateway.builder;

import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.dto.entity.Invoice;

@Component
public class InvoiceViewBuilder {

    public InvoiceView build(Invoice invoice) {
        var view = new InvoiceView();
        view.setId(invoice.getId());
        view.setSum(invoice.getSum());
        return view;
    }
}
