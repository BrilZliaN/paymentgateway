package ru.ifmo.practice.gateway.helper;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;

public class ExceptionFactory {

    public static PaymentGatewayException wrap(DataAccessException exception) {
        return wrap(exception, "Неверный запрос: ошибка базы данных");
    }

    public static PaymentGatewayException wrap(DataAccessException exception, String reason) {
        return wrap(exception, HttpStatus.BAD_REQUEST, reason);
    }

    public static PaymentGatewayException wrap(Exception exception) {
        return wrap(exception, HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
    }

    private static PaymentGatewayException wrap(Exception exception, HttpStatus httpStatus, String message) {
        var paymentGatewayException = newException(httpStatus, message);
        paymentGatewayException.initCause(exception);
        return paymentGatewayException;
    }

    public static PaymentGatewayException notFound() {
        return newException(HttpStatus.NOT_FOUND, "Объект не найден");
    }

    public static PaymentGatewayException newException(HttpStatus httpStatus, String message) {
        return new PaymentGatewayException(httpStatus, message);
    }
}
