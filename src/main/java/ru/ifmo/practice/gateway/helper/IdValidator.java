package ru.ifmo.practice.gateway.helper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class IdValidator {

    public void validate(long id) {
        if (id <= 0) {
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "Id должен быть больше 0");
        }
    }

}
