package ru.ifmo.practice.gateway.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.builder.InvoiceBuilder;
import ru.ifmo.practice.gateway.builder.InvoiceViewBuilder;
import ru.ifmo.practice.gateway.dto.entity.Invoice;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;

@Component
@RequiredArgsConstructor
public class InvoiceDaoAdapter {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceBuilder invoiceBuilder;
    private final InvoiceViewBuilder invoiceViewBuilder;

    public InvoiceView addInvoice(InvoicePostView invoiceView) {
        final var invoice = invoiceBuilder.build(invoiceView);
        try {
            invoiceRepository.save(invoice);
        } catch (DataAccessException dataAccessException) {
            throw ExceptionFactory.wrap(dataAccessException);
        }
        return invoiceViewBuilder.build(invoice);
    }

    public Invoice getInvoiceById(Long id) {
        try {
            return invoiceRepository.findById(id).orElseThrow(ExceptionFactory::notFound);
        } catch (DataAccessException dataAccessException) {
            throw ExceptionFactory.wrap(dataAccessException);
        }
    }
}
