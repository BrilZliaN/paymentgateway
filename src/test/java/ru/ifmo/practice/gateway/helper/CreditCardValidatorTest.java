package ru.ifmo.practice.gateway.helper;

import org.junit.jupiter.api.Test;
import ru.ifmo.practice.gateway.api.models.CreditCardView;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreditCardValidatorTest {

    private final Random random = new Random(System.currentTimeMillis());
    private final CardDataGenerator cardDataGenerator = new CardDataGenerator();

    CreditCardValidator creditCardValidator = new CreditCardValidator();

    private static int TEST_NUMBER = 20;
    private static int NUMBER_LENGTH = 16;


    private static String BAD_CHARS = "!@#$%^&*()1234567890";

    @Test
    void testCorrect() {
        for (int id = 1; id <= TEST_NUMBER; id++) {
            var number = cardDataGenerator.generateNumber();
            var name = cardDataGenerator.generateHolder();
            var date = cardDataGenerator.generateDate(id);
            String cvv = String.format("%03d", random.nextInt(1000));
            var card = new CreditCardView().number(number).holder(name).expirationDate(date).cvv(cvv);
            creditCardValidator.validate(card);
        }
    }

    @Test
    void testInvalidNumber() {
        for (int id = 1; id <= TEST_NUMBER; id++) {
            long number = (cardDataGenerator.generateNumber(Long.toString(random.nextInt(10000)), NUMBER_LENGTH) + 1)
                    % (long) Math.pow(10, NUMBER_LENGTH);
            var name = cardDataGenerator.generateHolder();
            var date = cardDataGenerator.generateDate(id);
            String cvv = String.format("%03d", random.nextInt(1000));
            var card = new CreditCardView().number(number).holder(name).expirationDate(date).cvv(cvv);
            Exception e = assertThrows(PaymentGatewayException.class, () -> {
                creditCardValidator.validate(card);
            });
            assertEquals(e.getMessage(), "неверный номер карты");
        }

        for (int id = 1; id <= TEST_NUMBER; id++) {
            long number = cardDataGenerator.generateNumber(Long.toString(random.nextInt(10000)), NUMBER_LENGTH - 1);
            var name = cardDataGenerator.generateHolder();
            var date = cardDataGenerator.generateDate(id);
            String cvv = String.format("%03d", random.nextInt(1000));
            var card = new CreditCardView().number(number).holder(name).expirationDate(date).cvv(cvv);
            Exception e = assertThrows(PaymentGatewayException.class, () -> {
                creditCardValidator.validate(card);
            });
            assertEquals(e.getMessage(), "номер карты должен содержать 16 символов");
        }
    }

    @Test
    void testInvalidName() {
        for (int id = 1; id <= TEST_NUMBER; id++) {
            var number = cardDataGenerator.generateNumber(Long.toString(random.nextInt(10000)), NUMBER_LENGTH);
            var name = cardDataGenerator.generateHolder() + BAD_CHARS.charAt(id % BAD_CHARS.length());
            var date = cardDataGenerator.generateDate(id);
            String cvv = String.format("%03d", random.nextInt(1000));
            var card = new CreditCardView().number(number).holder(name).expirationDate(date).cvv(cvv);
            Exception e = assertThrows(PaymentGatewayException.class, () -> {
                creditCardValidator.validate(card);
            });
            assertEquals(e.getMessage(), "неверный формат имени владельца");
        }
    }

    @Test
    void testInvalidDate() {
        for (int id = 1; id <= TEST_NUMBER; id++) {
            var number = cardDataGenerator.generateNumber();
            var name = cardDataGenerator.generateHolder();
            var date = cardDataGenerator.generateDate(-id);
            String cvv = String.format("%03d", random.nextInt(1000));
            var card = new CreditCardView().number(number).holder(name).expirationDate(date).cvv(cvv);
            Exception e = assertThrows(PaymentGatewayException.class, () -> {
                creditCardValidator.validate(card);
            });
            assertEquals(e.getMessage(), "срок действия карты истек");
        }
    }



}