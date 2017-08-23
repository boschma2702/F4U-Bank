package com.bank.service.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.exception.NotAuthorizedException;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.util.Constants;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.bank.util.systemvariable.SystemVariableNames.CARD_USAGE_ATTEMPTS;

@Service
public class CreditCardUnblockService {


    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    public void unblockCard(String pinCard, String iBAN) throws NoEffectException, NotAuthorizedException {
        Logger.info("Unblocking creditCard=%s", pinCard);
        CreditCardBean creditCardBean = creditCardRepository.getBlockedCardByCreditCardNumber(pinCard);
        if(creditCardBean == null || (creditCardBean.getAttempts() == 0)){
            Logger.error("Could not find blocked creditCard=%s", pinCard);
            throw new NoEffectException("Blocked card not present");
        }
        if(!creditCardBean.getAccountBean().getAccountNumber().equals(iBAN)){
            Logger.error("Could not unblock card, credit card and account do not match");
            throw new NotAuthorizedException("Credit card and account do not match");
        }
        creditCardBean.setAttempts(0);
        creditCardBean.setActive(true);
        creditCardRepository.save(creditCardBean);
    }
}
