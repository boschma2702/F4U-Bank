package com.bank.projection.account;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountAmountProjection {
    private double balance;
    private double savingAccountBalance;
    private Double credit;

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

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }
}
