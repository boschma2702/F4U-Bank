package com.bank.controller;

import com.bank.bean.account.AccountBean;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.exception.NotAuthorizedException;
import com.bank.projection.pin.PinProjection;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.service.AuthenticationService;
import com.bank.service.account.AccountService;
import com.bank.service.card.CardInvalidateService;
import com.bank.service.card.CardUnblockService;
import com.bank.service.creditcard.CreditCardInvalidateService;
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

    @Autowired
    private CreditCardInvalidateService creditCardInvalidateService;

    @Autowired
    private CreditCardRepository creditCardRepository;

    public void unblockCard(String authToken, String iBan, String pinCard) throws NotAuthorizedException, InvalidParamValueException, NoEffectException, AccountFrozenException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        boolean isCredit = CreditCardNumberChecker.isCreditCardNumber(pinCard);
        String normalizedIBAN = isCredit ? creditCardService.getCreditCardBeanCheckFrozen(pinCard).getAccountBean().getAccountNumber() : iBan;
        if (accountService.checkIfIsMainAccountHolderCheckFrozen(normalizedIBAN, customerId)) {
            if (isCredit) {
                creditCardUnblockService.unblockCard(pinCard, iBan);
            } else {
                int accountId = accountService.getAccountBeanByAccountNumber(iBan).getAccountId();
                cardUnblockService.unblockCard(accountId, pinCard);
            }
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public PinProjection invalidateCard(String authToken, String iBAN, String pinCard, boolean newPin) throws InvalidParamValueException, NotAuthorizedException, AccountFrozenException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        boolean isCredit = CreditCardNumberChecker.isCreditCardNumber(pinCard);
        String normalizedIBAN = isCredit ? creditCardService.getCreditCardBeanCheckFrozen(pinCard).getAccountBean().getAccountNumber() : iBAN;
        if (accountService.checkIfIsMainAccountHolderCheckFrozen(normalizedIBAN, customerId)) {
            if (isCredit) {
                return creditCardInvalidateService.invalidateCard(pinCard, newPin);
            } else {
                AccountBean accountBean = accountService.getAccountBeanByAccountNumber(iBAN);
                return cardInvalidateService.invalidateCard(accountBean.getAccountId(), customerId, pinCard, newPin);
            }
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

}
