package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ifmo.practice.gateway.api.models.TransactionView;
import ru.ifmo.practice.gateway.dto.entity.Invoice;
import ru.ifmo.practice.gateway.dto.entity.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TransactionsListBuilderTest {

    private static final int LIST_SIZE = 10;
    private static final Random RANDOM = new Random();
    private final TransactionsListBuilder transactionsListBuilder = new TransactionsListBuilder();

    public static List<Transaction> generateList() {
        List<Transaction> list = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            var transaction = new Transaction();
            transaction.setId((long) Math.abs(RANDOM.nextInt()));
            transaction.setCreated(LocalDateTime.now());
            transaction.setStatusCode(TransactionView.StatusEnum.PROCESSING.toString());
            transaction.setStatusDate(LocalDateTime.now());
            var invoice = new Invoice();
            invoice.setId((long) i);
            invoice.setSum(RANDOM.nextInt());
            transaction.setInvoice(invoice);
            list.add(transaction);
        }
        return list;
    }

    @Test
    public void test() {
        var list = generateList();
        var result = transactionsListBuilder.build(list);
        assertEquals(list.size(), result.size());
        for (int i = 0; i < list.size(); i++) {
            var expected = list.get(i);
            var actual = result.get(i);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getStatusCode(), actual.getStatus().toString());
            assertEquals(expected.getInvoice().getId(), actual.getInvoiceId());
            assertEquals(expected.getInvoice().getSum(), actual.getSum());
        }
    }

}