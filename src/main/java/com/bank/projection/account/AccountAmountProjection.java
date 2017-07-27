package com.bank.projection.account;

public class AccountAmountProjection {
    private double balance;
    private double savingAccountBalance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getSavingAccountBalance() {
        return savingAccountBalance;
    }

    public void setSavingAccountBalance(double savingAccountBalance) {
        this.savingAccountBalance = savingAccountBalance;
    }
}
