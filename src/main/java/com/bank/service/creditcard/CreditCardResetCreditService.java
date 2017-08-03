package com.bank.service.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.service.time.TimeService;
import com.bank.service.transaction.TransactionService;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CreditCardResetCreditService extends DayPassedListener {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public void onDayPassed(Date start, Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int amountOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (calendar.get(Calendar.DAY_OF_MONTH) == amountOfDaysInMonth) {
            resetCreditCardCredit();
        }
    }

    private void resetCreditCardCredit() {
        List<CreditCardBean> creditCardBeans = creditCardRepository.getCreditCardsWithUsedCredit(TimeService.TIMESIMULATOR.getCurrentDate());
        for (CreditCardBean creditCardBean : creditCardBeans) {
            String message = "Pay off credit card debt";
            BigDecimal amount = creditCardBean.getCreditLimit().subtract(creditCardBean.getCredit());
            transactionService.retrieveTransaction(creditCardBean.getAccountBean(), amount, message);
            creditCardBean.setCredit(creditCardBean.getCreditLimit());
            creditCardRepository.save(creditCardBean);
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
