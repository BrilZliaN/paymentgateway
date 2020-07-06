package ru.ifmo.practice.gateway.helper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoicePostView;

@Component
public class InvoicePostViewValidator {

    public void validate(InvoicePostView invoicePostView) {
        if (invoicePostView.getSum() < 0) {
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "Отрицательная сумма платежа");
        }
    }

}
