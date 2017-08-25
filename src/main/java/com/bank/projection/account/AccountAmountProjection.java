package com.bank.projection.account;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountAmountProjection {
    private double balance;
    private Double savingAccountBalance;
    private Double credit;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Double getSavingAccountBalance() {
        return savingAccountBalance;
    }

    public void setSavingAccountBalance(Double savingAccountBalance) {
        this.savingAccountBalance = savingAccountBalance;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }
}
