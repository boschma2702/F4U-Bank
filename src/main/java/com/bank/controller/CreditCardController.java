package com.bank.controller;

import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NotAllowedException;
import com.bank.exception.NotAuthorizedException;
import com.bank.projection.pin.PinProjection;
import com.bank.service.AuthenticationService;
import com.bank.service.account.AccountService;
import com.bank.service.creditcard.CreditCardCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardController {

    @Autowired
    private CreditCardCreateService creditCardCreateService;

    @Autowired
    private AccountService accountService;

    public PinProjection requestCreditCard(String authToken, String iBAN) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException, NotAllowedException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        if (accountService.checkIfIsMainAccountHolderCheckFrozen(iBAN, customerId)) {
            accountService.checkMinor(iBAN);
            return creditCardCreateService.createCreditCard(accountService.getAccountBeanByAccountNumber(iBAN).getAccountId());
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

}
