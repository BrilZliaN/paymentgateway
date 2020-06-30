package ru.ifmo.practice.gateway.builder;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class PaymentGatewayErrorBuilderTest {

    private final int TEST_NUMBER = 100;
    private final PaymentGatewayErrorBuilder builder = new PaymentGatewayErrorBuilder();
    private final String BASIC_MESSAGE = "message ";


    @Test
    void test() {
        for (int i = 0; i < TEST_NUMBER; i++) {
            var exception = new PaymentGatewayException(HttpStatus.OK, BASIC_MESSAGE + i);
            var error = builder.build(exception);
            assertEquals(error.getMessage(), BASIC_MESSAGE + i);
        }
    }

}