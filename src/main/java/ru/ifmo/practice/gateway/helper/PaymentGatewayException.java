package ru.ifmo.practice.gateway.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class PaymentGatewayException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;

}
