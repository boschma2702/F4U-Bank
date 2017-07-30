package com.bank.service.card;

import com.bank.bean.card.CardBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.card.CardRepository;
import com.bank.service.time.TimeService;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public CardBean getCardBean(String pinCard, int accountId) throws InvalidParamValueException {
        Logger.info("Retrieving CardBean of accountId=%s and pinCard=%s", accountId, pinCard);
        CardBean cardBean = cardRepository.getCardBean(accountId, pinCard, TimeService.TIMESIMULATOR.getCurrentDate());
        if(cardBean==null){
            Logger.error("Could not retrieve pinCard=%s of accountId=%s", pinCard, accountId);
            throw new InvalidParamValueException("Unknown pinCard");
        }
        return cardBean;
    }

}
