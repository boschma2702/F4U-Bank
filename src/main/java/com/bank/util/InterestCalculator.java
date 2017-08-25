package com.bank.util;

public class InterestCalculator {

    public static double getInterest(int amountOfDaysInMonth, double amount, double annualInterest) {
        double monthlyRate = Math.pow((1 + annualInterest), 1f / 12) - 1;
        double dailyRate = monthlyRate / amountOfDaysInMonth;
        return amount * dailyRate;
    }

}
