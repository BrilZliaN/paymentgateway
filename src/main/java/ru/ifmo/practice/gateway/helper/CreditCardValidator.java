package ru.ifmo.practice.gateway.helper;

import org.springframework.http.HttpStatus;
import ru.ifmo.practice.gateway.api.models.CreditCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreditCardValidator {

    public static void validate(CreditCardView creditCardView) {
        validateNumber(creditCardView.getNumber());
        validateExpirationDate(creditCardView.getExpirationDate());
        validateHolder(creditCardView.getHolder());
        validateLuhn(creditCardView.getNumber().toString());
    }

    private static void validateNumber(Long number) {
        if (number.toString().length() != 16) {
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "номер карты должен содержать 16 символов");
        }
    }

    private static void validateExpirationDate(String expirationDate) {
        SimpleDateFormat parser = new SimpleDateFormat("mm/yy");
        try {
            var expire = parser.parse(expirationDate);
            var now = new Date();
            if (expire.compareTo(now) < 0) {
                throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "срок действия карты истек");
            }
        } catch (ParseException e) {
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "неверный формат срока действия карты");
        }
    }

    private static void validateHolder(String holder) {
        if (!holder.matches("^((?:[A-Z]+ ?){1,3})$")) {
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "неверный формат имени владельца");
        }
    }

    static void validateLuhn(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        if (nSum % 10 != 0) {
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "неверный номер карты");
        }
    }

}
