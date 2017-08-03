package com.bank.service.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.NoEffectException;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.util.Constants;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardUnblockService {


    @Autowired
    private CreditCardRepository creditCardRepository;

    public void unblockCard(String pinCard) throws NoEffectException {
        Logger.info("Unblocking creditCard=%s", pinCard);
        CreditCardBean creditCardBean = creditCardRepository.getBlockedCardByCreditCardNumber(pinCard);
        if(creditCardBean == null || !(creditCardBean.getAttempts() == Constants.CARD_BLOCK_LIMIT)){
            Logger.error("Could not find blocked creditCard=%s", pinCard);
            throw new NoEffectException("Blocked card not present");
        }
        creditCardBean.setAttempts(0);
        creditCardBean.setActive(true);
        creditCardRepository.save(creditCardBean);
    }
}
