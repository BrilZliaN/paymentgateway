package ru.ifmo.practice.gateway.logic.bank;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceOperationIntegrationTest {

    @Autowired
    private CreateInvoiceOperation createInvoiceOperation;

    @Autowired
    private GetInvoiceOperation getInvoiceOperation;

    private final Random random = new Random(System.currentTimeMillis());

    @Test
    public void testCreateAndGet() {
        double sum = Math.abs(random.nextInt());
        var invoicePostView = new InvoicePostView().sum(sum);

        var invoiceView = createInvoiceOperation.process(invoicePostView);

        assertEquals(invoiceView.getSum(), invoicePostView.getSum());

        long id = invoiceView.getId();

        var nextInvoiceView = getInvoiceOperation.process(id);

        assertEquals(nextInvoiceView.getSum(), invoiceView.getSum());
        assertEquals(nextInvoiceView.getId(), invoiceView.getId());
    }

    @Test
    public void testCreateIncorrect() {
        double sum = Math.abs(random.nextInt()) * (-1);
        var invoicePostView = new InvoicePostView().sum(sum);

        var exception = assertThrows(PaymentGatewayException.class, () -> {
            createInvoiceOperation.process(invoicePostView);
        });

        assertEquals(exception.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testCreateAndGetNonExistent() {
        double sum = Math.abs(random.nextInt());
        var invoicePostView = new InvoicePostView().sum(sum);
        var invoiceView = createInvoiceOperation.process(invoicePostView);

        long id = invoiceView.getId() + 5;
        var exception = assertThrows(PaymentGatewayException.class, () -> {
            getInvoiceOperation.process(id);
        });

        assertEquals(exception.getHttpStatus(), HttpStatus.NOT_FOUND);
    }


}
