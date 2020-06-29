package ru.ifmo.practice.gateway.builder;

import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;

@Component
public class InvoicePostViewBuilder {

    public InvoicePostView build(double sum) {
        return new InvoicePostView().sum(sum);
    }
}
