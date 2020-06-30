package ru.ifmo.practice.gateway.helper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.CreditCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CreditCardValidator {

    public void validate(CreditCardView creditCardView) {
        validateNumber(creditCardView.getNumber());
        validateExpirationDate(creditCardView.getExpirationDate());
        validateHolder(creditCardView.getHolder());
        validateLuhn(creditCardView.getNumber().toString());
    }

    private void validateNumber(Long number) {
        if (number.toString().length() != 16) {
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "номер карты должен содержать 16 символов");
        }
    }

    private void validateExpirationDate(String expirationDate) {
        var parser = new SimpleDateFormat("MM/yy");
        parser.setLenient(false);
        try {
            var expire = parser.parse(expirationDate);
            if (expire.before(new Date())) {
                throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "срок действия карты истек");
            }
        } catch (ParseException e) {
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "неверный формат срока действия карты");
        }
    }

    private void validateHolder(String holder) {
        if (!holder.matches("^((?:[A-Z]+ ?){1,3})$")) {
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "неверный формат имени владельца");
        }
    }

    private void validateLuhn(String cardNumber) {
        var result = validateLuhn(cardNumber);
        if (!result) {
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "неверный номер карты");
        }
    }

    public static boolean validateLuhn(String cardNumber) {
        int nDigits = cardNumber.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = cardNumber.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return nSum % 10 == 0;
    }

}
