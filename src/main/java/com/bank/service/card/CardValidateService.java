package com.bank.service.card;

import com.bank.bean.card.CardBean;
import com.bank.exception.InvalidPINException;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.card.CardRepository;
import com.bank.service.time.TimeService;
import com.bank.util.Constants;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardValidateService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    public CardBean validateCard(int accountId, String pinCard, String pinCode) throws InvalidPINException, InvalidParamValueException {
        Logger.info("Validating pinCard=%s of accountId=%s", pinCard, accountId);
        CardBean bean = cardService.getCardBean(pinCard, accountId);
        if(bean.getPinCode().equals(pinCode)){
            if(bean.getAttempts()!=0){
                bean.setAttempts(0);
                cardRepository.save(bean);
            }
        }else{
            Logger.error("Invalid combination of pin and pinCard=%s of accountId=%s", pinCard, accountId);
            if(bean.getAttempts() == Constants.CARD_BLOCK_LIMIT){
                Logger.info("Attempting to pay with blocked account, pinCard=%s of accountId=%s", pinCard, accountId);
            }else{
                bean.setAttempts(bean.getAttempts()+1);
                if(bean.getAttempts() == Constants.CARD_BLOCK_LIMIT){
                    bean.setActive(false);
                }
            }
            cardRepository.save(bean);
            throw new InvalidPINException("Invalid pin information");
        }

        return bean;
    }
}
