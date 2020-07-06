package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InvoiceBuilderTest {

    private static final int TEST_NUMBER = 100;
    private final Random random = new Random(System.currentTimeMillis());
    private final InvoiceBuilder invoiceBuilder = new InvoiceBuilder();

    @Test
    void test() {
        for (int i = 0; i < TEST_NUMBER; i++) {
            double sum = Math.abs(random.nextInt());
            var invoicePostView = new InvoicePostView().sum(sum);
            var invoice = invoiceBuilder.build(invoicePostView);
            assertEquals(invoice.getSum(), sum);
        }
    }

}