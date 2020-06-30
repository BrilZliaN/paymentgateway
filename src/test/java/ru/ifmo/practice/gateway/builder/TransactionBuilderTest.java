package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.dto.entity.Card;
import ru.ifmo.practice.gateway.dto.entity.Invoice;
import ru.ifmo.practice.gateway.helper.CardDataGenerator;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionBuilderTest {

    private final static int TEST_NUMBER = 100;
    private final TransactionBuilder builder = new TransactionBuilder();
    private final CardDataGenerator cardDataGenerator = new CardDataGenerator();

    @Test
    void testBuild() {
        for (int i = 0; i < TEST_NUMBER; i++) {
            var card = generateCard(i + 1);
            var invoice = generateInvoice();
            var transaction = builder.build(card, invoice);
            assertEquals(transaction.getCard(), card);
            assertEquals(transaction.getInvoice(), invoice);
            assertTrue(transaction.getStatusDate().isEqual(LocalDateTime.now())
                    || transaction.getStatusDate().isBefore(LocalDateTime.now()));
        }
    }

    @Test
    void testUpdate() {
        for (int i = 0; i < TEST_NUMBER; i++) {
            var card = generateCard(i + 1);
            var invoice = generateInvoice();
            var transaction = builder.build(card, invoice);
            var time = transaction.getStatusDate();
            builder.update(transaction, new TransactionStatusView().answerCode("testing"));
            assertEquals(transaction.getStatusCode(), "testing");
            assertTrue(transaction.getStatusDate().isEqual(LocalDateTime.now())
                    || transaction.getStatusDate().isAfter(time));
        }
    }

    private Card generateCard(int shift) {
        var card = new Card();
        card.setNumber(cardDataGenerator.generateNumber());
        card.setOwner(cardDataGenerator.generateHolder());
        card.setValid(cardDataGenerator.generateDate(shift));
        return card;
    }

    private Invoice generateInvoice() {
        var invoice = new Invoice();
        invoice.setSum(Math.abs(new Random(System.currentTimeMillis()).nextInt()));
        invoice.setCreated(LocalDateTime.now());
        return invoice;
    }
}