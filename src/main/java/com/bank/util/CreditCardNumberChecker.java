package com.bank.util;

public class CreditCardNumberChecker {


    public static boolean isCreditCardNumber(String creditCardNumber) {
        return creditCardNumber.length() == 16 && isNumeric(creditCardNumber);
    }

    private static boolean isNumeric(String string) {
        char[] chars = string.toCharArray();
        for (char c : chars) {
            if (Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

}
