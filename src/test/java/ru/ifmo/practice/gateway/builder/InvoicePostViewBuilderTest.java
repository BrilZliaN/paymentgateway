package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InvoicePostViewBuilderTest {

    private final InvoicePostViewBuilder invoicePostViewBuilder = new InvoicePostViewBuilder();

    @Test
    public void test() {
        double sum = Math.abs(new Random().nextInt());
        var result = invoicePostViewBuilder.build(sum);
        assertEquals(sum, result.getSum());
    }

}