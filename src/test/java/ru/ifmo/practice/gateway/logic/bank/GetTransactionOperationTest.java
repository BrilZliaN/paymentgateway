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
import ru.ifmo.practice.gateway.dto.entity.Transaction;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;
import ru.ifmo.practice.gateway.helper.IdValidator;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

import java.time.LocalDateTime;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GetTransactionOperation.class, IdValidator.class})
public class GetTransactionOperationTest {

    private final Random random = new Random(System.currentTimeMillis());
    @Autowired
    private GetTransactionOperation getTransactionOperation;
    @Autowired
    private IdValidator idValidator;
    @MockBean
    private TransactionDaoAdapter transactionDaoAdapter;

    @Test
    public void testCorrect() {
        long id = Math.abs(random.nextInt());
        final Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setCreated(LocalDateTime.now());
        Mockito.when(transactionDaoAdapter.getTransactionByInvoiceId(id)).thenReturn(transaction);
        var result = getTransactionOperation.process(id);
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).getTransactionByInvoiceId(id);
        Assertions.assertEquals(transaction.getId(), result.getId());
        Assertions.assertEquals(result.getCreated(), transaction.getCreated());
    }

    @Test
    public void testIncorrectId() {
        long id = Math.abs(random.nextInt()) * (-1);
        final Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setCreated(LocalDateTime.now());
        Mockito.when(transactionDaoAdapter.getTransactionByInvoiceId(id)).thenReturn(transaction);
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            getTransactionOperation.process(id);
        });
        Mockito.verify(transactionDaoAdapter, Mockito.times(0)).getTransactionByInvoiceId(id);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testDatabaseException() {
        long id = Math.abs(random.nextInt());
        Mockito.when(transactionDaoAdapter.getTransactionByInvoiceId(id)).thenThrow(ExceptionFactory.wrap(
                new DataAccessException("error") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                }));
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            getTransactionOperation.process(id);
        });
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).getTransactionByInvoiceId(id);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testServerError() {
        long id = Math.abs(random.nextInt());
        Mockito.when(transactionDaoAdapter.getTransactionByInvoiceId(id)).thenThrow(ExceptionFactory.wrap(new Exception()));
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            getTransactionOperation.process(id);
        });
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).getTransactionByInvoiceId(id);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }
}