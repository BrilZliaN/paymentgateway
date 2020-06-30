package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ifmo.practice.gateway.dto.entity.Invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class InvoiceViewBuilderTest {

    private final Random random = new Random(System.currentTimeMillis());
    private final int TEST_NUMBER = 100;
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
            assertEquals(invoiceView.getId(), i);
            assertEquals(invoiceView.getSum(), sum);
        }
    }

}