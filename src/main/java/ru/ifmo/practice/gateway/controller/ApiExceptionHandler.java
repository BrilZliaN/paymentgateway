package ru.ifmo.practice.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.ifmo.practice.gateway.builder.PaymentGatewayErrorBuilder;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;
import ru.ifmo.practice.gateway.helper.PaymentGatewayError;
import ru.ifmo.practice.gateway.helper.PaymentGatewayException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PaymentGatewayException.class)
    public final ResponseEntity<PaymentGatewayError> handle(PaymentGatewayException exception, WebRequest request) {
        exception.printStackTrace();
        return new ResponseEntity<>(PaymentGatewayErrorBuilder.build(exception), exception.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<PaymentGatewayError> handle(Exception exception, WebRequest request) {
        exception.printStackTrace();
        var wrapped = ExceptionFactory.wrap(exception);
        return new ResponseEntity<>(PaymentGatewayErrorBuilder.build(wrapped), wrapped.getHttpStatus());
    }

}