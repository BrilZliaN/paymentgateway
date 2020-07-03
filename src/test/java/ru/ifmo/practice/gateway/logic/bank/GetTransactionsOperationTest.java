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
import ru.ifmo.practice.gateway.builder.TransactionsListBuilder;
import ru.ifmo.practice.gateway.builder.TransactionsListBuilderTest;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

import ru.ifmo.practice.gateway.api.models.TransactionsList;
import ru.ifmo.practice.gateway.api.models.TransactionView;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GetTransactionsOperation.class, TransactionsListBuilder.class})
public class GetTransactionsOperationTest {

    @Autowired
    private GetTransactionsOperation getTransactionsOperation;

    @Autowired
    private TransactionsListBuilder transactionsListBuilder;

    @MockBean
    private TransactionDaoAdapter transactionDaoAdapter;

    @Test
    public void testOk() {
        var transactions = TransactionsListBuilderTest.generateList();
        var expectedList = new TransactionsList();
        transactions.stream().map(transaction -> {
            var view = new TransactionView();
            view.setId(transaction.getId());
            view.setInvoiceId(transaction.getInvoice().getId());
            view.setSum(transaction.getInvoice().getSum());
            return view;
        }).forEach(expectedList::add);
        Mockito.when(transactionDaoAdapter.getAllTransactions()).thenReturn(transactions);
        var resultList = getTransactionsOperation.process();
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).getAllTransactions();
        assertEquals(expectedList.size(), resultList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            var expected = expectedList.get(i);
            var result = resultList.get(i);
            assertEquals(expected.getId(), result.getId());
            assertEquals(expected.getInvoiceId(), result.getInvoiceId());
            assertEquals(expected.getSum(), result.getSum());
        }
    }

    @Test
    public void testDatabaseException() {
        Mockito.when(transactionDaoAdapter.getAllTransactions()).thenThrow(ExceptionFactory.wrap(
                new DataAccessException("error") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }));
        var exception = Assertions.assertThrows(PaymentGatewayException.class, getTransactionsOperation::process);
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).getAllTransactions();
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testServerError() {
        Mockito.when(transactionDaoAdapter.getAllTransactions()).thenThrow(ExceptionFactory.wrap(new Exception()));
        var exception = Assertions.assertThrows(PaymentGatewayException.class, getTransactionsOperation::process);
        Mockito.verify(transactionDaoAdapter, Mockito.times(1)).getAllTransactions();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }
}