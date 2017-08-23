package com.bank.service.creditcard;

import com.bank.bean.account.AccountBean;
import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.pin.CardProjection;
import com.bank.projection.pin.PinProjection;
import com.bank.repository.account.AccountRepository;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.service.time.TimeService;
import com.bank.util.RandomStringGenerator;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

import static com.bank.util.systemvariable.SystemVariableNames.CARD_EXPIRATION_LENGTH;
import static com.bank.util.systemvariable.SystemVariableNames.CARD_USAGE_ATTEMPTS;

@Service
public class CreditCardCreateService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardNumberGenerator creditCardNumberGenerator;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    public PinProjection createCreditCard(int accountId) throws InvalidParamValueException {
        return createCreditCard(accountId, RandomStringGenerator.generateRandomIntegerString(4));
    }

    public PinProjection createCreditCard(int accountId, String pinCode) throws InvalidParamValueException {
        Logger.info("Creating credit card for accountId=%s", accountId);
        if(creditCardRepository.hasAccountIdCreditCard(accountId, TimeService.TIMESIMULATOR.getCurrentDate(), (Integer) systemVariableRetrieveService.getObjectInternally(CARD_USAGE_ATTEMPTS))){
            Logger.error("Could not create credit card, accountId=%s already has active account", accountId);
            throw new InvalidParamValueException("Already active credit card present");
        }
        AccountBean accountBean = accountRepository.findAccountBeansByAccountId(accountId);
        CreditCardBean creditCardBean = new CreditCardBean();
        creditCardBean.setCreditCardNumber(creditCardNumberGenerator.generateCreditCardNumber());
        creditCardBean.setCreditCardPin(pinCode);
        creditCardBean.setAccountBean(accountBean);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeService.TIMESIMULATOR.getCurrentDate());
        calendar.add(Calendar.YEAR, (Integer) systemVariableRetrieveService.getObjectInternally(CARD_EXPIRATION_LENGTH));
        creditCardBean.setExpirationDate(calendar.getTime());
        creditCardRepository.save(creditCardBean);

        PinProjection cardProjection = new PinProjection();
        cardProjection.setPinCard(creditCardBean.getCreditCardNumber());
        cardProjection.setPinCode(creditCardBean.getCreditCardPin());
        cardProjection.setExpirationDate(creditCardBean.getExpirationDate());
        return cardProjection;
    }

}
