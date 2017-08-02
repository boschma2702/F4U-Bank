package com.bank.service.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.InvalidPINException;
import com.bank.exception.InvalidParamValueException;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardValidateService {

    @Autowired
    private CreditCardService creditCardService;

    public CreditCardBean validateCreditCard(String creditCardNumber, String pinCode) throws InvalidParamValueException, InvalidPINException {
        Logger.info("Validating creditCardNumber=%s", creditCardNumber);
        CreditCardBean creditCardBean = creditCardService.getCreditCardBeanByCreditCardNumber(creditCardNumber);
        if(!creditCardBean.getCreditCardPin().equals(pinCode)){
            Logger.error("Invalid combination of creditCardId=%s and pinCode", creditCardBean.getCreditCardId());
            throw new InvalidPINException("Invalid credit card number and pincode");
        }
        return creditCardBean;
    }

}
