package ru.ifmo.practice.gateway.logic.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;
import ru.ifmo.practice.gateway.helper.InvoicePostViewValidator;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.InvoiceDaoAdapter;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CreateInvoiceOperation.class, InvoicePostViewValidator.class})
public class CreateInvoiceOperationTest {

    private final Random random = new Random(System.currentTimeMillis());
    @MockBean
    private InvoiceDaoAdapter invoiceDaoAdapter;
    @Autowired
    private CreateInvoiceOperation createInvoiceOperation;
    @Autowired
    private InvoicePostViewValidator invoicePostViewValidator;

    @Test
    public void createCorrect() {
        long id = Math.abs(random.nextInt());
        double sum = Math.abs(random.nextInt());

        var invoice = new InvoicePostView().sum(sum);

        var expectedResult = new InvoiceView();
        expectedResult.setId(id);
        expectedResult.setSum(sum);

        Mockito.when(invoiceDaoAdapter.addInvoice(invoice)).thenReturn(expectedResult);

        var result = createInvoiceOperation.process(invoice);

        Mockito.verify(invoiceDaoAdapter, Mockito.times(1)).addInvoice(invoice);

        assertEquals(result.getId(), Long.valueOf(id));
        assertEquals(result.getSum(), Double.valueOf(sum));
    }

    @Test
    public void createIncorrectSum() {
        long id = Math.abs(random.nextInt());
        double sum = Math.abs(random.nextInt()) * (-1);

        var invoice = new InvoicePostView().sum(sum);

        var expectedResult = new InvoiceView();
        expectedResult.setId(id);
        expectedResult.setSum(sum);

        Mockito.when(invoiceDaoAdapter.addInvoice(invoice)).thenReturn(expectedResult);

        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            createInvoiceOperation.process(invoice);
        });

        Mockito.verify(invoiceDaoAdapter, Mockito.times(0)).addInvoice(invoice);

        assertEquals(exception.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDatabaseException() {
        long id = Math.abs(random.nextInt());
        double sum = Math.abs(random.nextInt());

        var invoice = new InvoicePostView().sum(sum);

        Mockito.when(invoiceDaoAdapter.addInvoice(invoice)).thenThrow(ExceptionFactory.wrap(
                new DataAccessException("error") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                }));

        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            createInvoiceOperation.process(invoice);
        });

        Mockito.verify(invoiceDaoAdapter, Mockito.times(1)).addInvoice(invoice);

        assertEquals(exception.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testServerError() {
        long id = Math.abs(random.nextInt());
        double sum = Math.abs(random.nextInt());

        var invoice = new InvoicePostView().sum(sum);

        Mockito.when(invoiceDaoAdapter.addInvoice(invoice)).thenThrow(ExceptionFactory.wrap(new Exception()));

        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            createInvoiceOperation.process(invoice);
        });

        Mockito.verify(invoiceDaoAdapter, Mockito.times(1)).addInvoice(invoice);

        assertEquals(exception.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}