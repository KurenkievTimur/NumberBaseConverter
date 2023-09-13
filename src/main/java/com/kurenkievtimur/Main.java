package com.kurenkievtimur;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

import static java.lang.Character.LETTER_NUMBER;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            String[] radix = scanner.nextLine().split(" ");

            if (radix[0].equalsIgnoreCase("/exit"))
                break;

            while (true) {
                int sourceRadix = Integer.parseInt(radix[0]);
                int targetRadix = Integer.parseInt(radix[1]);

                System.out.printf("Enter number in base %d to convert to base %d (To go back type /back) ", sourceRadix, targetRadix);
                String stringNumber = scanner.nextLine();

                if (stringNumber.equalsIgnoreCase("/back")) {
                    System.out.println();
                    break;
                }

                String[] numberParts = stringNumber.split("\\.");

                String result = "";
                if (numberParts.length == 2) {
                    String bigDecimal = toBigDecimal(numberParts, sourceRadix);

                    String[] decimalParts = bigDecimal.split("\\.");

                    String decimal = decimalToTargetBase(decimalParts[0], targetRadix);
                    String fraction = decimalFractionToTargetBase(decimalParts[1], targetRadix);

                    result = "%s.%s".formatted(decimal, fraction);
                } else {
                    String decimal = toDecimal(numberParts[0], sourceRadix);
                    result = decimalToTargetBase(decimal, targetRadix);
                }

                System.out.printf("Conversion result: %s%n%n", result);
            }
        }
    }

    public static String toBigDecimal(String[] numberParts, int radix) {
        String decimal = numberParts[0];
        String fraction = numberParts[1];

        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (int i = 0, k = decimal.length() - 1; i < decimal.length(); i++, k--) {
            int numericValue = Character.getNumericValue(decimal.charAt(i));

            double multiply = numericValue * Math.pow(radix, k);
            bigDecimal = bigDecimal.add(BigDecimal.valueOf(multiply));
        }

        for (int i = 0, k = -1; i < fraction.length(); i++, k--) {
            int numericValue = Character.getNumericValue(fraction.charAt(i));

            double multiply = numericValue * Math.pow(radix, k);
            bigDecimal = bigDecimal.add(BigDecimal.valueOf(multiply));
        }

        return bigDecimal.toPlainString();
    }

    public static String toDecimal(String number, int radix) {
        return new BigInteger(number, radix).toString(LETTER_NUMBER);
    }

    public static String decimalToTargetBase(String number, int radix) {
        return new BigInteger(number).toString(radix);
    }

    public static String decimalFractionToTargetBase(String number, int radix) {
        BigDecimal decimal = new BigDecimal("0.%s".formatted(number));
        StringBuilder builder = new StringBuilder();

        int k = 5;
        while (--k >= 0) {
            decimal = decimal.multiply(BigDecimal.valueOf(radix));
            int integerPart = decimal.intValue();

            builder.append(Character.forDigit(integerPart, radix));
            decimal = decimal.subtract(BigDecimal.valueOf(integerPart));
        }

        return builder.toString();
    }
}
