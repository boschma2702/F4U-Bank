package com.bank.controller;

import com.bank.bean.account.AccountBean;
import com.bank.exception.AuthenticationException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.exception.NotAuthorizedException;
import com.bank.projection.pin.CardProjection;
import com.bank.service.AuthenticationService;
import com.bank.service.account.AccountService;
import com.bank.service.card.CardInvalidateService;
import com.bank.service.card.CardUnblockService;
import com.bank.service.creditcard.CreditCardService;
import com.bank.service.creditcard.CreditCardUnblockService;
import com.bank.util.CreditCardNumberChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardController {

    @Autowired
    private CardUnblockService cardUnblockService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardInvalidateService cardInvalidateService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private CreditCardUnblockService creditCardUnblockService;

    public void unblockCard(String authToken, String iBan, String pinCard) throws NotAuthorizedException, InvalidParamValueException, NoEffectException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        boolean isCredit = CreditCardNumberChecker.isCreditCardNumber(pinCard);
        String normalizedIBAN = isCredit ? creditCardService.getCreditCardBean(pinCard).getAccountBean().getAccountNumber() : iBan;
        if(accountService.checkIfIsMainAccountHolder(normalizedIBAN, customerId)){
            if(isCredit){
                creditCardUnblockService.unblockCard(pinCard);
            }else{
                int accountId = accountService.getAccountBeanByAccountNumber(iBan).getAccountId();
                cardUnblockService.unblockCard(accountId, pinCard);
            }
        }else{
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public CardProjection invalidateCard(String authToken, String iBAN, String pinCard, boolean newPin) throws InvalidParamValueException, NotAuthorizedException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        if(accountService.checkIfIsMainAccountHolder(iBAN, customerId)){
            AccountBean accountBean = accountService.getAccountBeanByAccountNumber(iBAN);
            return cardInvalidateService.invalidateCard(accountBean.getAccountId(), customerId, pinCard, newPin);
        }else{
            throw new NotAuthorizedException("Not Authorized");
        }
    }

}
