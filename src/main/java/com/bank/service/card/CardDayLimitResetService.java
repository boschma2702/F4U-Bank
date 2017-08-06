package com.bank.service.card;

import com.bank.bean.card.CardBean;
import com.bank.repository.card.CardRepository;
import com.bank.service.time.TimeService;
import com.bank.util.logging.Logger;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CardDayLimitResetService extends DayPassedListener {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public void onDayPassed(Date start, Date end) {
        Logger.info("Resetting card limit remaining");
        cardRepository.resetCardDayLimitRemaining(TimeService.TIMESIMULATOR.getCurrentDate());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
