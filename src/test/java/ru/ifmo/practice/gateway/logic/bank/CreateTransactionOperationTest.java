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
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.dto.entity.Invoice;
import ru.ifmo.practice.gateway.helper.*;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CreateTransactionOperation.class, CreditCardValidator.class, IdValidator.class})
public class CreateTransactionOperationTest {

    private final Random random = new Random(System.currentTimeMillis());
    private final TransactionDataGenerator transactionDataGenerator = new TransactionDataGenerator();
    @Autowired
    private CreateTransactionOperation createTransactionOperation;
    @Autowired
    private CreditCardValidator creditCardValidator;
    @Autowired
    private IdValidator idValidator;
    @MockBean
    private TransactionDaoAdapter transactionDaoAdapter;
    private CreditCardView creditCardView;
    private long invoiceId;


    @Test
    public void createCorrectTransaction() {
        var creditCardView = transactionDataGenerator.generateCreditCardView();
        long invoiceId = Math.abs(random.nextInt());
        var invoice = transactionDataGenerator.generateInvoice(invoiceId);
        var transaction = transactionDataGenerator.constructTransaction(invoice, creditCardView);

        Mockito.when(transactionDaoAdapter.createTransaction(invoiceId, creditCardView)).thenReturn(transaction);

        var result = createTransactionOperation.process(invoiceId, creditCardView);

        Mockito.verify(transactionDaoAdapter, Mockito.times(1))
                .createTransaction(invoiceId, creditCardView);

        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getStatusDate(), result.getStatusDate());
        assertEquals(transaction.getCard(), result.getCard());
        assertEquals(transaction.getStatusCode(), result.getStatusCode());
        assertEquals(transaction.getInvoice(), result.getInvoice());
        assertEquals(transaction.getCreated(), result.getCreated());
    }

    @Test
    public void createTransactionWithIncorrectInvoiceId() {
        var creditCardView = transactionDataGenerator.generateCreditCardView();
        long invoiceId = Math.abs(random.nextInt()) * (-1);
        var invoice = transactionDataGenerator.generateInvoice(invoiceId);

        processDataFailure(invoice, creditCardView);
    }

    @Test
    public void createTransactionWithIncorrectCreditData() {
        var creditCardView = transactionDataGenerator.generateCreditCardView();
        long invoiceId = Math.abs(random.nextInt()) * (-1);
        creditCardView.setNumber(creditCardView.getNumber() + 1);
        var invoice = transactionDataGenerator.generateInvoice(invoiceId);

        processDataFailure(invoice, creditCardView);
    }

    private void processDataFailure(Invoice invoice, CreditCardView creditCardView) {
        var transaction = transactionDataGenerator.constructTransaction(invoice, creditCardView);
        var invoiceId = invoice.getId();

        Mockito.when(transactionDaoAdapter.createTransaction(invoiceId, creditCardView)).thenReturn(transaction);

        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            createTransactionOperation.process(invoiceId, creditCardView);
        });

        Mockito.verify(transactionDaoAdapter, Mockito.times(0))
                .createTransaction(invoiceId, creditCardView);

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testDatabaseError() {
        var creditCardView = transactionDataGenerator.generateCreditCardView();
        long invoiceId = Math.abs(random.nextInt());
        var invoice = transactionDataGenerator.generateInvoice(invoiceId);
        var transaction = transactionDataGenerator.constructTransaction(invoice, creditCardView);

        Mockito.when(transactionDaoAdapter.createTransaction(invoiceId, creditCardView)).thenThrow(ExceptionFactory.wrap(
                new DataAccessException("error") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                }
        ));

        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            createTransactionOperation.process(invoiceId, creditCardView);
        });

        Mockito.verify(transactionDaoAdapter, Mockito.times(1))
                .createTransaction(invoiceId, creditCardView);

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testServerError() {
        var creditCardView = transactionDataGenerator.generateCreditCardView();
        long invoiceId = Math.abs(random.nextInt());
        var invoice = transactionDataGenerator.generateInvoice(invoiceId);
        var transaction = transactionDataGenerator.constructTransaction(invoice, creditCardView);

        Mockito.when(transactionDaoAdapter.createTransaction(invoiceId, creditCardView)).thenThrow(ExceptionFactory.wrap(new Exception()));

        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            createTransactionOperation.process(invoiceId, creditCardView);
        });

        Mockito.verify(transactionDaoAdapter, Mockito.times(1))
                .createTransaction(invoiceId, creditCardView);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }
}