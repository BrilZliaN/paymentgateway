package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvoicePostViewBuilderTest {

    private final InvoicePostViewBuilder builder = new InvoicePostViewBuilder();

    @Test
    public void test() {
        var view = builder.build(100d);
        assertEquals(100d, view.getSum());
    }
}
