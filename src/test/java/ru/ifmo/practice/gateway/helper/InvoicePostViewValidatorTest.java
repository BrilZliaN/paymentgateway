package ru.ifmo.practice.gateway.helper;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;

import javax.swing.*;

import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class InvoicePostViewValidatorTest {

    private final InvoicePostViewValidator invoicePostViewValidator = new InvoicePostViewValidator();

    private final Random random = new Random(System.currentTimeMillis());

    @Test
    public void testIncorrect() {
        var invoice = new InvoicePostView().sum(Math.abs(random.nextInt()) * (-1.0));
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            invoicePostViewValidator.validate(invoice);
        });
        assertEquals(exception.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testCorrect() {
        var invoice = new InvoicePostView().sum((double) Math.abs(random.nextInt()));
        invoicePostViewValidator.validate(invoice);
    }


}