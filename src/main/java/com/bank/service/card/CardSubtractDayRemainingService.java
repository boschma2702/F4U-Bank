package com.bank.service.card;

import com.bank.bean.card.CardBean;
import com.bank.repository.card.CardRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CardSubtractDayRemainingService {

    @Autowired
    private CardRepository cardRepository;

    public void subtractDayRemaining(CardBean cardBean, BigDecimal amount) {
        Logger.info("Subtracting day remaining day limit from cardId=%s", cardBean.getCardId());
        cardBean.setDayLimitRemaining(cardBean.getDayLimitRemaining().subtract(amount));
        cardRepository.save(cardBean);
    }

}
