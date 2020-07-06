package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ifmo.practice.gateway.api.models.CreditCardView;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CardBuilderTest {

    private static final int TEST_NUMBER = 100;
    private static final long BASIC_NUMBER = 1000000000;
    private static final String BASIC_NAME = "JOHN SMITH ";
    CardBuilder cardBuilder = new CardBuilder();

    @Test
    void test() {
        for (int id = 0; id < TEST_NUMBER; id++) {
            long number = BASIC_NUMBER + id;
            String holder = BASIC_NAME + id;
            String date = (id % 13) + "/" + (id / 100);
            String cvv = String.format("%03d", id);
            var creditCardView = new CreditCardView().number(number).holder(holder).expirationDate(date).cvv(cvv);
            var card = cardBuilder.build(creditCardView);
            assertEquals(card.getNumber(), number);
            assertEquals(card.getValid(), date);
            assertEquals(card.getOwner(), holder);
        }
    }

}