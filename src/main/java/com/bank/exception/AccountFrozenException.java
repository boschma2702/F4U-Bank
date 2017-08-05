package com.bank.exception;

public class AccountFrozenException extends Exception{
    public AccountFrozenException(String message) {
        super(message);
    }
}
