package ru.ifmo.practice.gateway.logic.bank;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ifmo.practice.gateway.dto.entity.Transaction;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;
import ru.ifmo.practice.gateway.helper.IdValidator;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GetTransactionOperation.class, IdValidator.class})
public class GetTransactionOperationTest {

    @Autowired
    private GetTransactionOperation getTransactionOperation;

    @Autowired
    private IdValidator idValidator;

    @MockBean
    private TransactionDaoAdapter transactionDaoAdapter;

    private final Random random = new Random(System.currentTimeMillis());

    @Test
    public void testCorrect() {
        long id = Math.abs(random.nextInt());
        final Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setCreated(LocalDateTime.now());
        Mockito.when(transactionDaoAdapter.getTransactionById(id)).thenReturn(transaction);
        var result = getTransactionOperation.process(id);
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).getTransactionById(id);
        Assertions.assertEquals(transaction.getId(), result.getId());
        Assertions.assertEquals(result.getCreated(), transaction.getCreated());
    }

    @Test
    public void testIncorrectId() {
        long id = Math.abs(random.nextInt()) * (-1);
        final Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setCreated(LocalDateTime.now());
        Mockito.when(transactionDaoAdapter.getTransactionById(id)).thenReturn(transaction);
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            getTransactionOperation.process(id);
        });
        Mockito.verify(transactionDaoAdapter, Mockito.times(0)).getTransactionById(id);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testDatabaseException() {
        long id = Math.abs(random.nextInt());
        Mockito.when(transactionDaoAdapter.getTransactionById(id)).thenThrow(ExceptionFactory.wrap(
                new DataAccessException("error") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                }));
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            getTransactionOperation.process(id);
        });
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).getTransactionById(id);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testServerError() {
        long id = Math.abs(random.nextInt());
        Mockito.when(transactionDaoAdapter.getTransactionById(id)).thenThrow(ExceptionFactory.wrap(new Exception()));
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            getTransactionOperation.process(id);
        });
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).getTransactionById(id);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }
}