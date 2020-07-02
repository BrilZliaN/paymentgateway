package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvoiceBuilderTest {

    private final Random random = new Random(System.currentTimeMillis());
    private static final int TEST_NUMBER = 100;
    private final InvoiceBuilder invoiceBuilder = new InvoiceBuilder();

    @Test
    void test() {
        for (int i = 0; i < TEST_NUMBER; i++) {
            double sum = Math.abs(random.nextInt());
            var invoicePostView = new InvoicePostView().sum(sum);
            var invoice = invoiceBuilder.build(invoicePostView);
            assertEquals(sum, invoice.getSum());
        }
    }

}