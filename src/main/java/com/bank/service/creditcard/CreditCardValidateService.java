package com.bank.service.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.InvalidPINException;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.bank.util.systemvariable.SystemVariableNames.CARD_USAGE_ATTEMPTS;

@Service
public class CreditCardValidateService {

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    public CreditCardBean validateCreditCard(String creditCardNumber, String pinCode) throws InvalidParamValueException, InvalidPINException {
        Logger.info("Validating creditCardNumber=%s", creditCardNumber);
        CreditCardBean creditCardBean = creditCardService.getCreditCardBeanByCreditCardNumber(creditCardNumber, true);

        if (creditCardBean.getCreditCardPin().endsWith(pinCode)) {
            if (creditCardBean.getAttempts() != 0) {
                creditCardBean.setAttempts(0);
                creditCardRepository.save(creditCardBean);
            }
        } else {
            if (creditCardBean.getAttempts() == (int) systemVariableRetrieveService.getObjectInternally(CARD_USAGE_ATTEMPTS)) {
                Logger.info("Attempting to pay with blocked credit card, creditCardId=%s", creditCardBean.getCreditCardId());
            } else {
                creditCardBean.setAttempts(creditCardBean.getAttempts() + 1);
                if (creditCardBean.getAttempts() == (int) systemVariableRetrieveService.getObjectInternally(CARD_USAGE_ATTEMPTS)) {
                    creditCardBean.setActive(false);
                }
            }
            creditCardRepository.save(creditCardBean);
            Logger.error("Invalid combination of creditCardId=%s and pinCode", creditCardBean.getCreditCardId());
            throw new InvalidPINException("Invalid credit card number and pincode");
        }
        return creditCardBean;
    }

}
