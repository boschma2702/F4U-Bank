package com.bank.service.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
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
            creditCardBean = creditCardRepository.findActiveCreditCardBeanByCreditCardNumber(creditCardNumber, TimeService.TIMESIMULATOR.getCurrentDate());
        } else {
            creditCardBean = creditCardRepository.findActiveCreditCardBeanByCreditCardNumber(creditCardNumber);
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

    public CreditCardBean getCreditCardBeanByAccountId(int accountId, boolean pastActivationDate) throws InvalidParamValueException {
        Logger.info("Retrieving credit card of accountId=%s", accountId);
        CreditCardBean creditCardBean;
        if (pastActivationDate) {
            creditCardBean = creditCardRepository.getCreditCardBeanByAccountId(accountId, TimeService.TIMESIMULATOR.getCurrentDate());
        } else {
            creditCardBean = creditCardRepository.getCreditCardBeanByAccountId(accountId);
        }
        if (creditCardBean == null) {
            Logger.error("Could not retrieve credit card of accountId=%s", accountId);
            throw new InvalidParamValueException("Could not find credit card");
        }
        return creditCardBean;
    }

}
