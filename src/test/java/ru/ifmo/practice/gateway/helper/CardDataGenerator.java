package ru.ifmo.practice.gateway.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CardDataGenerator {

    private final Random random = new Random(System.currentTimeMillis());

    private static String[] NAMES = {"FIRST", "SECOND", "THIRD", "FOURTH", "FIFTH",
            "SIXTH", "SEVENTH", "EIGHT", "NINTH", "TENTH"};

    private static int BASIC_NUMBER_LENGTH = 16;

    public String generateDate(int shift) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, shift);

        var simpleDateFormat = new SimpleDateFormat("MM/yy");
        return simpleDateFormat.format(calendar.getTime());
    }

    public String generateHolder() {
        return NAMES[random.nextInt(NAMES.length)] + " " + NAMES[random.nextInt(NAMES.length)];
    }

    public Long generateNumber() {
        return generateNumber(Integer.toString(random.nextInt(10000)), BASIC_NUMBER_LENGTH);
    }

    public Long generateNumber(String bin, int length) {
        int randomNumberLength = length - (bin.length() + 1);

        StringBuilder builder = new StringBuilder(bin);
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = this.random.nextInt(10);
            builder.append(digit);
        }

        int checkDigit = getCheckDigit(builder.toString());
        builder.append(checkDigit);

        return Long.parseLong(builder.toString());
    }

    private int getCheckDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }
        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }
}
