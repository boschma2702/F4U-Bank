package com.bank.service.creditcard;

import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreditCardUpdateAmountService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    public void updateAmount(int creditCardId, BigDecimal amount){
        Logger.info("Updating credit card credit of CreditCardId=%s", creditCardId);
        creditCardRepository.updateAmount(creditCardId, amount);
    }

}
