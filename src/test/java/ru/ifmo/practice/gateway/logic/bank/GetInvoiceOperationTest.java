package ru.ifmo.practice.gateway.logic.bank;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ifmo.practice.gateway.builder.InvoiceViewBuilder;
import ru.ifmo.practice.gateway.dto.entity.Invoice;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;
import ru.ifmo.practice.gateway.helper.IdValidator;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.InvoiceDaoAdapter;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {InvoiceViewBuilder.class, GetInvoiceOperation.class, IdValidator.class})
public class GetInvoiceOperationTest {

    private final Random random = new Random(System.currentTimeMillis());

    @Autowired
    private GetInvoiceOperation getInvoiceOperation;

    @Autowired
    private InvoiceViewBuilder invoiceViewBuilder;

    @Autowired
    private IdValidator idValidator;

    @MockBean
    private InvoiceDaoAdapter invoiceDaoAdapter;


    @Test
    public void testGetInvoice() {
        long id = Math.abs(random.nextInt());
        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setSum(random.nextDouble());
        invoice.setCreated(LocalDateTime.now());

        Mockito.when(invoiceDaoAdapter.getInvoiceById(id)).thenReturn(invoice);

        var result = getInvoiceOperation.process(id);

        Mockito.verify(invoiceDaoAdapter, Mockito.times(1)).getInvoiceById(id);

        assertEquals(result.getId(), invoice.getId());
        assertEquals(result.getSum(), invoice.getSum());
    }

    @Test
    public void testFailure() {
        long id = Math.abs(random.nextInt());
        Mockito.when(invoiceDaoAdapter.getInvoiceById(id)).thenThrow(ExceptionFactory.notFound());

        var exception = assertThrows(PaymentGatewayException.class, () -> {
            getInvoiceOperation.process(id);
        });

        Mockito.verify(invoiceDaoAdapter, Mockito.times(1)).getInvoiceById(id);

        assertEquals(exception.getHttpStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testInvalidId() {
        long id = Math.abs(random.nextInt()) * (-1);
        Mockito.when(invoiceDaoAdapter.getInvoiceById(id)).thenThrow(ExceptionFactory.notFound());

        var exception = assertThrows(PaymentGatewayException.class, () -> {
            getInvoiceOperation.process(id);
        });

        Mockito.verify(invoiceDaoAdapter, Mockito.times(0)).getInvoiceById(id);

        assertEquals(exception.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testServerError() {
        long id = Math.abs(random.nextInt());
        Mockito.when(invoiceDaoAdapter.getInvoiceById(id)).thenThrow(ExceptionFactory.wrap(new Exception()));

        var exception = assertThrows(PaymentGatewayException.class, () -> {
            getInvoiceOperation.process(id);
        });

        Mockito.verify(invoiceDaoAdapter, Mockito.times(1)).getInvoiceById(id);

        assertEquals(exception.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDatabaseError() {
        long id = Math.abs(random.nextInt());
        Mockito.when(invoiceDaoAdapter.getInvoiceById(id)).thenThrow(ExceptionFactory.wrap(
                new DataAccessException("error") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                }));

        var exception = assertThrows(PaymentGatewayException.class, () -> {
            getInvoiceOperation.process(id);
        });

        Mockito.verify(invoiceDaoAdapter, Mockito.times(1)).getInvoiceById(id);

        assertEquals(exception.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

}