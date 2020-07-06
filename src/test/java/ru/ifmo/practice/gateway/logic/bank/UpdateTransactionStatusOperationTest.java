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
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;
import ru.ifmo.practice.gateway.helper.IdValidator;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UpdateTransactionStatusOperation.class, IdValidator.class})
public class UpdateTransactionStatusOperationTest {

    @Autowired
    private UpdateTransactionStatusOperation updateTransactionStatusOperation;

    @Autowired
    private IdValidator idValidator;

    @MockBean
    private TransactionDaoAdapter transactionDaoAdapter;

    private final Random random = new Random(System.currentTimeMillis());

    @Test
    public void testCorrect() {
        long id = Math.abs(random.nextInt());
        var status = new TransactionStatusView().answerCode("status");
        updateTransactionStatusOperation.process(id, status);
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).updateTransactionStatus(id, status);
    }

    @Test
    public void testIncorrectId() {
        long id = Math.abs(random.nextInt()) * (-1);
        var status = new TransactionStatusView().answerCode("status");
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            updateTransactionStatusOperation.process(id, status);
        });
        Mockito.verify(transactionDaoAdapter, Mockito.times(0)).updateTransactionStatus(id, status);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void createNonExistentTransaction() {
        long id = Math.abs(random.nextInt());
        var status = new TransactionStatusView().answerCode("status");
        Mockito.doThrow(ExceptionFactory.notFound()).when(transactionDaoAdapter).updateTransactionStatus(id, status);
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            updateTransactionStatusOperation.process(id, status);
        });
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).updateTransactionStatus(id, status);
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testDatabaseException() {
        long id = Math.abs(random.nextInt());
        var status = new TransactionStatusView().answerCode("status");
        Mockito.doThrow(ExceptionFactory.wrap(new DataAccessException("error") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        })).when(transactionDaoAdapter).updateTransactionStatus(id, status);
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            updateTransactionStatusOperation.process(id, status);
        });
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).updateTransactionStatus(id, status);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testServerError() {
        long id = Math.abs(random.nextInt());
        var status = new TransactionStatusView().answerCode("status");
        Mockito.doThrow(ExceptionFactory.wrap(new Exception())).when(transactionDaoAdapter).updateTransactionStatus(id, status);
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            updateTransactionStatusOperation.process(id, status);
        });
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).updateTransactionStatus(id, status);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }


}