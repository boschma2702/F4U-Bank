package com.bank.service.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.service.time.TimeService;
import com.bank.service.time.TimeSimulateService;
import com.bank.util.logging.Logger;
import com.bank.util.time.TimeSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    public CreditCardBean getCreditCardBeanByCreditCardNumber(String creditCardNumber) throws InvalidParamValueException {
        Logger.info("Retrieving credit card with creditCardNumber=%s", creditCardNumber);
        CreditCardBean creditCardBean = creditCardRepository.findCreditCardBeanByCreditCardNumber(creditCardNumber, TimeService.TIMESIMULATOR.getCurrentDate());
        if(creditCardBean == null){
            Logger.error("Could not retrieve credit card with creditCardNumber=%s", creditCardNumber);
            throw new InvalidParamValueException("Could not find credit card");
        }
        return creditCardBean;
    }

}
