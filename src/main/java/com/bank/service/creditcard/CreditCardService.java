package com.bank.service.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.service.time.TimeService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    public CreditCardBean getCreditCardBeanByCreditCardNumber(String creditCardNumber, boolean pastActivationDate) throws InvalidParamValueException {
        Logger.info("Retrieving credit card with creditCardNumber=%s", creditCardNumber);
        CreditCardBean creditCardBean;
        if (pastActivationDate) {
            creditCardBean = creditCardRepository.findActiveCreditCardBeanAfterActivationByCreditCardNumber(creditCardNumber, TimeService.TIMESIMULATOR.getCurrentDate());
        } else {
            creditCardBean = creditCardRepository.findActiveCreditCardBeanByCreditCardNumber(creditCardNumber, TimeService.TIMESIMULATOR.getCurrentDate());
        }

        if (creditCardBean == null) {
            Logger.error("Could not retrieve credit card with creditCardNumber=%s", creditCardNumber);
            throw new InvalidParamValueException("Could not find credit card");
        }
        return creditCardBean;
    }

    public CreditCardBean getCreditCardBean(String creditCardNumber) throws InvalidParamValueException {
        CreditCardBean creditCardBean = creditCardRepository.findCreditCardBeanByCreditCardNumber(creditCardNumber);
        if(creditCardBean == null){
            Logger.error("Could not retrieve credit card with creditCardNumber=%s", creditCardNumber);
            throw new InvalidParamValueException("Could not find credit card");
        }
        return creditCardBean;
    }

    public CreditCardBean getCreditCardBeanCheckFrozen(String creditCardNumber) throws InvalidParamValueException, AccountFrozenException {
        CreditCardBean creditCardBean = creditCardRepository.findCreditCardBeanByCreditCardNumber(creditCardNumber);
        if(creditCardBean == null){
            Logger.error("Could not retrieve credit card with creditCardNumber=%s", creditCardNumber);
            throw new InvalidParamValueException("Could not find credit card");
        }
        if(creditCardBean.getAccountBean().isFrozen()){
            Logger.error("Could not retrieve credit cardCardNumber=%s, account is frozen", creditCardNumber);
            throw new AccountFrozenException("Account is frozen");
        }
        return creditCardBean;
    }


    public CreditCardBean getCreditCardBeanByAccountId(int accountId, boolean pastActivationDate) throws InvalidParamValueException {
        Logger.info("Retrieving credit card of accountId=%s", accountId);
        CreditCardBean creditCardBean;
        if (pastActivationDate) {
            creditCardBean = creditCardRepository.getCreditCardBeanAfterActivationByAccountId(accountId, TimeService.TIMESIMULATOR.getCurrentDate());
        } else {
            creditCardBean = creditCardRepository.getCreditCardBeanByAccountId(accountId, TimeService.TIMESIMULATOR.getCurrentDate());
        }
        if (creditCardBean == null) {
            Logger.error("Could not retrieve credit card of accountId=%s", accountId);
            throw new InvalidParamValueException("Could not find credit card");
        }
        return creditCardBean;
    }

}
