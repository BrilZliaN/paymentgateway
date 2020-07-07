package ru.ifmo.practice.gateway.builder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PaymentGatewayErrorBuilderTest {

    private static final int TEST_NUMBER = 100;
    private static final String BASIC_MESSAGE = "message ";
    private final PaymentGatewayErrorBuilder builder = new PaymentGatewayErrorBuilder();

    @Test
    void test() {
        for (int i = 0; i < TEST_NUMBER; i++) {
            var exception = new PaymentGatewayException(HttpStatus.OK, BASIC_MESSAGE + i);
            var error = builder.build(exception);
            assertEquals(error.getMessage(), BASIC_MESSAGE + i);
        }
    }

}