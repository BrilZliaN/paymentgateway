package ru.ifmo.practice.gateway.helper;

import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.builder.CardBuilder;
import ru.ifmo.practice.gateway.dto.entity.Invoice;
import ru.ifmo.practice.gateway.dto.entity.Transaction;

import java.time.LocalDateTime;
import java.util.Random;

public class TransactionDataGenerator {

    private final Random random = new Random(System.currentTimeMillis());

    private final CardDataGenerator cardDataGenerator = new CardDataGenerator();

    public CreditCardView generateCreditCardView(long invoiceId) {
        var creditCardView = new CreditCardView();
        creditCardView.setNumber(cardDataGenerator.generateNumber());
        creditCardView.setHolder(cardDataGenerator.generateHolder());
        creditCardView.setExpirationDate(cardDataGenerator.generateDate(1));
        creditCardView.setCvv(Integer.toString(random.nextInt(1000)));
        creditCardView.setInvoiceId(invoiceId);
        return creditCardView;
    }

    public CreditCardView generateCreditCardView() {
        return generateCreditCardView((long) Math.abs(random.nextInt()));
    }

    public Invoice generateInvoice(long invoiceId) {
        var invoice = new Invoice();
        invoice.setCreated(LocalDateTime.now());
        invoice.setSum(Math.abs(random.nextInt()));
        invoice.setId(invoiceId);
        return invoice;
    }

    public Transaction constructTransaction(Invoice invoice, CreditCardView creditCardView) {
        var transaction = new Transaction();
        transaction.setStatusCode("PROCESSING");
        transaction.setStatusDate(LocalDateTime.now());
        transaction.setCard(new CardBuilder().build(creditCardView));
        transaction.setId((long) Math.abs(random.nextInt()));
        transaction.setInvoice(invoice);
        transaction.setCreated(LocalDateTime.now());
        return transaction;
    }

}
