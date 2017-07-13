package com.bank.controller;

import com.bank.exception.AuthenticationException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.exception.NotAuthorizedException;
import com.bank.service.AuthenticationService;
import com.bank.service.account.AccountService;
import com.bank.service.card.CardUnblockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardController {

    @Autowired
    private CardUnblockService cardUnblockService;

    @Autowired
    private AccountService accountService;

    public void unblockCard(String authToken, String iBan, String pinCard) throws NotAuthorizedException, InvalidParamValueException, NoEffectException {
        try {
            int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
            if(accountService.checkIfIsMainAccountHolder(iBan, customerId)){
                int accountId = accountService.getAccountBeanByAccountNumber(iBan).getAccountId();
                cardUnblockService.unblockCard(accountId, pinCard);
            }else{
                throw new NotAuthorizedException("Not Authorized");
            }
        } catch (AuthenticationException e) {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

}
