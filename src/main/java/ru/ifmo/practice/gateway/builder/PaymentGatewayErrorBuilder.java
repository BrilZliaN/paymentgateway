package ru.ifmo.practice.gateway.builder;

import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.helper.PaymentGatewayError;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;

import java.util.Objects;

@Component
public class PaymentGatewayErrorBuilder {

    public PaymentGatewayError build(PaymentGatewayException exception) {
        var error = new PaymentGatewayError();
        var message = exception.getMessage();
        error.setMessage(Objects.requireNonNullElse(message, "Ошибка транзакции"));
        return error;
    }

}
