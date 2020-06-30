package ru.ifmo.practice.gateway.logic.bank;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.helper.TransactionDataGenerator;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionOperationIntegrationTest {

    @Autowired
    private GetTransactionOperation getTransactionOperation;

    @Autowired
    private CreateTransactionOperation createTransactionOperation;

    @Autowired
    private UpdateTransactionStatusOperation updateTransactionStatusOperation;

    @Autowired
    private CreateInvoiceOperation createInvoiceOperation;

    private final Random random = new Random(System.currentTimeMillis());

    private final TransactionDataGenerator transactionDataGenerator = new TransactionDataGenerator();

    private long currentInvoiceId;

    private InvoiceView createInvoice() {
        double sum = Math.abs(random.nextInt());
        var invoicePostView = new InvoicePostView().sum(sum);
        return createInvoiceOperation.process(invoicePostView);
    }

    @Test
    public void createAndGetTransaction() {
        var invoiceView = createInvoice();
        var creditCardView = transactionDataGenerator.generateCreditCardView(invoiceView.getId());
        var transaction = createTransactionOperation.process(invoiceView.getId(), creditCardView);

        Assertions.assertEquals(invoiceView.getId(), transaction.getInvoice().getId());
        Assertions.assertEquals(creditCardView.getNumber(), transaction.getCard().getNumber());
        Assertions.assertEquals(creditCardView.getExpirationDate(), transaction.getCard().getValid());
        Assertions.assertEquals(creditCardView.getHolder(), transaction.getCard().getOwner());

        var getTransaction = getTransactionOperation.process(transaction.getId());

        Assertions.assertEquals(transaction.getId(), getTransaction.getId());
        Assertions.assertEquals(transaction.getInvoice().getId(), getTransaction.getInvoice().getId());
        Assertions.assertEquals(transaction.getStatusCode(), getTransaction.getStatusCode());
        Assertions.assertEquals(transaction.getCard().getId(), getTransaction.getCard().getId());
    }

    @Test
    public void createAndGetNonExistent() {
        var invoiceView = createInvoice();
        var creditCardView = transactionDataGenerator.generateCreditCardView(invoiceView.getId());
        var transaction = createTransactionOperation.process(invoiceView.getId(), creditCardView);

        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            getTransactionOperation.process(transaction.getId() + 10);
        });

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testCreateTransactionWithIncorrectId() {
        var invoiceView = createInvoice();
        var creditCardView = transactionDataGenerator.generateCreditCardView(invoiceView.getId());
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            createTransactionOperation.process(invoiceView.getId() * (-1), creditCardView);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testGetTransactionWithIncorrectId() {
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            getTransactionOperation.process(Math.abs(random.nextInt()) * (-1));
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void updateAndGetTransaction() {
        var invoiceView = createInvoice();
        var creditCardView = transactionDataGenerator.generateCreditCardView(invoiceView.getId());
        var transaction = createTransactionOperation.process(invoiceView.getId(), creditCardView);

        var status = new TransactionStatusView().answerCode("next_status");
        updateTransactionStatusOperation.process(transaction.getId(), status);
        var newTransaction = getTransactionOperation.process(transaction.getId());

        Assertions.assertEquals(transaction.getId(), newTransaction.getId());
        Assertions.assertEquals(transaction.getCard().getId(), newTransaction.getCard().getId());
        Assertions.assertEquals(transaction.getInvoice().getId(), newTransaction.getInvoice().getId());
        Assertions.assertNotEquals(transaction.getStatusCode(), newTransaction.getStatusCode());
        Assertions.assertEquals(status.getAnswerCode(), newTransaction.getStatusCode());
    }

    @Test
    public void testUpdateNonExistentTransaction() {
        var invoiceView = createInvoice();
        var creditCardView = transactionDataGenerator.generateCreditCardView(invoiceView.getId());
        var transaction = createTransactionOperation.process(invoiceView.getId(), creditCardView);
        var status = new TransactionStatusView().answerCode("next_status");
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            updateTransactionStatusOperation.process(transaction.getId() + 10, status);
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testUpdateWithIncorrectId() {
        var invoiceView = createInvoice();
        var creditCardView = transactionDataGenerator.generateCreditCardView(invoiceView.getId());
        var transaction = createTransactionOperation.process(invoiceView.getId(), creditCardView);
        var status = new TransactionStatusView().answerCode("next_status");
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            updateTransactionStatusOperation.process(transaction.getId() * (-1), status);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

}
