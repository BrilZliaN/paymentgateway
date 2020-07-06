package ru.ifmo.practice.gateway.helper;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
public class IdValidatorTest {

    private final Random random = new Random(System.currentTimeMillis());

    private final IdValidator idValidator = new IdValidator();

    @Test
    public void testCorrect() {
        long id = Math.abs(random.nextInt());
        idValidator.validate(id);
    }

    @Test
    public void testIncorrect() {
        processIncorrectId(Math.abs(random.nextInt()) * (-1));
    }

    @Test
    public void testRandom() {
        long id = random.nextLong();
        if (id > 0) {
            idValidator.validate(id);
        } else {
            processIncorrectId(id);
        }
    }

    private void processIncorrectId(long id) {
        var exception = Assertions.assertThrows(PaymentGatewayException.class, () -> {
            idValidator.validate(id);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

}