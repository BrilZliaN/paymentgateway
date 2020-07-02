package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentGatewayErrorBuilderTest {

    private static final int TEST_NUMBER = 100;
    private final PaymentGatewayErrorBuilder builder = new PaymentGatewayErrorBuilder();
    private static final String BASIC_MESSAGE = "message ";


    @Test
    void test() {
        for (int i = 0; i < TEST_NUMBER; i++) {
            var exception = new PaymentGatewayException(HttpStatus.OK, BASIC_MESSAGE + i);
            var error = builder.build(exception);
            assertEquals( BASIC_MESSAGE + i, error.getMessage());
        }
    }

}