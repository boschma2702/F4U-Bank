package com.bank.controller;

import com.bank.exception.AuthenticationException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NotAuthorizedException;
import com.bank.service.AuthenticationService;
import com.bank.service.account.AccountService;
import com.bank.service.accountsaving.AccountSavingOpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountSavingController {


    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountSavingOpenService accountSavingOpenService;

    public void openSavingsAccount(String authToken, String iBAN) throws NotAuthorizedException, InvalidParamValueException {
        try {
            int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
            if (accountService.checkIfIsMainAccountHolder(iBAN, customerId)) {
                accountSavingOpenService.openSavingsAccount(iBAN);
            } else {
                throw new NotAuthorizedException("Not Authorized");
            }
        } catch (AuthenticationException e) {
            throw new NotAuthorizedException("Not Authorized");
        }
    }
}
