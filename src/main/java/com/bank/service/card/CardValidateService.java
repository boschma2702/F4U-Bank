package com.bank.service.card;

import com.bank.bean.card.CardBean;
import com.bank.exception.InvalidPINException;
import com.bank.repository.card.CardRepository;
import com.bank.service.time.TimeService;
import com.bank.util.Constants;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardValidateService {
    @Autowired
    private CardRepository cardRepository;

    public CardBean validateCard(int accountId, String pinCard, String pinCode) throws InvalidPINException {
        Logger.info("Validating pinCard=%s of accountId=%s", pinCard, accountId);
        CardBean bean = cardRepository.getCardBean(accountId, pinCard, TimeService.TIMESIMULATOR.getCurrentDate());
        if (bean == null) {
            Logger.error("Could not find pinCard=%s of accountId=%s or pinCard is invalidated", pinCard, accountId);
            throw new InvalidPINException("Invalid pin information");
        }

        if(bean.getPinCode().equals(pinCode)){
            if(bean.getAttempts()!=0){
                bean.setAttempts(0);
                cardRepository.save(bean);
            }
        }else{
            Logger.error("Invalid combination of pin and pinCard=%s of accountId=%s", pinCard, accountId);
            if(bean.getAttempts()== Constants.CARD_BLOCK_LIMIT){
                Logger.warn("pinCard=%s of accountId=%s reached limit of invalid attempts, card gets blocked", pinCard, accountId);
                bean.setActive(false);
            }else {
                bean.setAttempts(bean.getAttempts()+1);
            }
            cardRepository.save(bean);
            throw new InvalidPINException("Invalid pin information");
        }

        return bean;
    }
}
