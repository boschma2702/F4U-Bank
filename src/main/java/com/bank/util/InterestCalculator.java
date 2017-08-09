package com.bank.util;

public class InterestCalculator {

    public static double getInterest(int amountOfDaysInMonth, double amount, double interest) {
        return (Math.pow((1 + Math.pow((1 + interest), (1.0 * 1 / 12)) - 1), (1.0 * 1 / amountOfDaysInMonth)) - 1) * amount;
    }

}
