package com.bank.controller;

import com.bank.exception.AccountFrozenException;
import com.bank.exception.AuthenticationException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NotAuthorizedException;
import com.bank.service.AuthenticationService;
import com.bank.service.account.AccountService;
import com.bank.service.account.accountsaving.AccountSavingCloseService;
import com.bank.service.account.accountsaving.AccountSavingOpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountSavingController {


    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountSavingOpenService accountSavingOpenService;

    @Autowired
    private AccountSavingCloseService accountSavingCloseService;

    public void openSavingsAccount(String authToken, String iBAN) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        if (accountService.checkIfAccountHolderCheckFrozen(iBAN, customerId)) {
            accountSavingOpenService.openSavingsAccount(iBAN);
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

}
