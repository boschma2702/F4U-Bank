package com.bank.service.account.accountsaving;

import com.bank.bean.acountsavings.AccountSavingBean;
import com.bank.repository.accountsaving.AccountSavingRepository;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.util.InterestCalculator;
import com.bank.util.logging.Logger;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import static com.bank.util.systemvariable.SystemVariableNames.*;

@Service
public class AccountSavingInterestService extends DayPassedListener {

    @Autowired
    private AccountSavingRepository accountSavingRepository;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    @Override
    public void onDayPassed(Date start, Date end) {
        Logger.info("Calculate overdraft over savings account");

        Calendar day = Calendar.getInstance();
        day.setTime(start);
        int amountOfDaysInMonth = day.getActualMaximum(Calendar.DAY_OF_MONTH);

        Iterator<AccountSavingBean> iterator = accountSavingRepository.findAll().iterator();
        while (iterator.hasNext()) {
            AccountSavingBean accountSavingBean = iterator.next();
            double buildUpInterest = accountSavingBean.getBuildUpInterest();
            double interest = InterestCalculator.getInterest(amountOfDaysInMonth, accountSavingBean.getMinimumDayAmount(), getInterestPercentage(accountSavingBean.getMinimumDayAmount()));
            Logger.info("Interest of accountId=%s is overdraft=%s", accountSavingBean.getAccountBean().getAccountId(), interest);
            accountSavingBean.setBuildUpInterest(buildUpInterest + interest);
            accountSavingRepository.save(accountSavingBean);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private double getInterestPercentage(double amount) {
        if (amount > 0 && amount < 75000) {
            return (double) systemVariableRetrieveService.getObjectInternally(INTEREST_RATE_1);
        } else if (amount <= 75000 && amount < 1000000) {
            return (double) systemVariableRetrieveService.getObjectInternally(INTEREST_RATE_2);
        }
        return (double) systemVariableRetrieveService.getObjectInternally(INTEREST_RATE_3);
    }


}
