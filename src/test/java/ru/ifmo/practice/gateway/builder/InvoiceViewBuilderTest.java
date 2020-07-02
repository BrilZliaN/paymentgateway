package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;
import ru.ifmo.practice.gateway.dto.entity.Invoice;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvoiceViewBuilderTest {

    private final Random random = new Random(System.currentTimeMillis());
    private static final int TEST_NUMBER = 100;
    private final InvoiceViewBuilder invoiceViewBuilder = new InvoiceViewBuilder();

    @Test
    void test() {
        for (int i = 0; i < TEST_NUMBER; i++) {
            double sum = random.nextInt();
            LocalDateTime dateTime = LocalDateTime.now();
            var invoice = new Invoice();
            invoice.setId((long) i);
            invoice.setCreated(dateTime);
            invoice.setSum(sum);
            var invoiceView = invoiceViewBuilder.build(invoice);
            assertEquals(i, invoiceView.getId());
            assertEquals(sum, invoiceView.getSum());
        }
    }

}