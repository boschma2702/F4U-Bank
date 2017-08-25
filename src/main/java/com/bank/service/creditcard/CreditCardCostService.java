package com.bank.service.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.service.time.TimeService;
import com.bank.service.transaction.TransactionService;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bank.util.systemvariable.SystemVariableNames.CREDIT_CARD_MONTHLY_FEE;

@Service
public class CreditCardCostService extends DayPassedListener {

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Override
    public void onDayPassed(Date start, Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int amountOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (calendar.get(Calendar.DAY_OF_MONTH) == amountOfDaysInMonth) {
            payCreditCardCosts();
        }
    }

    private void payCreditCardCosts() {
        List<CreditCardBean> creditCardBeans = creditCardRepository.getAllActiveCreditCards(TimeService.TIMESIMULATOR.getCurrentDate());
        for (CreditCardBean creditCardBean : creditCardBeans) {
            String message = "Credit card costs";
            transactionService.retrieveTransaction(creditCardBean.getAccountBean(), (BigDecimal) systemVariableRetrieveService.getObjectInternally(CREDIT_CARD_MONTHLY_FEE), message);
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
