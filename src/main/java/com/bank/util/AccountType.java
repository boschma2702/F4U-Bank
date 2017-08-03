package com.bank.util;

public enum AccountType {

    NORMAL, SAVING, CREDIT;

    public static AccountType getAccountType(String accountNumber) {
        if (accountNumber.endsWith("S")) {
            return SAVING;
        } else if (accountNumber.endsWith("C")) {
            return CREDIT;
        } else {
            return NORMAL;
        }
    }

    public static String getNormalizedAccount(String accountNumber) {
        if (accountNumber.endsWith("S") || accountNumber.endsWith("C")) {
            return accountNumber.substring(0, accountNumber.length() - 1);
        } else {
            return accountNumber;
        }
    }
}
