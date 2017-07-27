package com.bank.service.accountsaving;

import com.bank.bean.acountsavings.AccountSavingBean;
import com.bank.repository.accountsaving.AccountSavingRepository;
import com.bank.service.time.TimeService;
import com.bank.util.Logging.Logger;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

@Service
public class AccountSavingInterestService extends DayPassedListener {

    @Autowired
    private AccountSavingRepository accountSavingRepository;

    @Override
    public void onDayPassed(Date start, Date end) {
        Logger.info("Calculate interest over savings account");

        Calendar day = Calendar.getInstance();
        day.setTime(start);
        int amountOfDaysInMonth = day.getActualMaximum(Calendar.DAY_OF_MONTH);

        Iterator<AccountSavingBean> iterator = accountSavingRepository.findAll().iterator();
        while (iterator.hasNext()) {
            AccountSavingBean accountSavingBean = iterator.next();
            double buildUpInterest = accountSavingBean.getBuildUpInterest();
            double interest = getInterest(amountOfDaysInMonth, accountSavingBean.getMinimumDayAmount());
            Logger.info("Interest of accountId=%s is interest=%s", accountSavingBean.getAccountBean().getAccountId(), interest);
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
            return 0.15e-2;
        } else if (amount <= 75000 && amount < 1000000) {
            return 0.2e-2;
        }
        return 0;
    }

    private double getInterest(int amountOfDaysInMonth, double amount) {
        return (Math.pow((1 + Math.pow((1 + getInterestPercentage(amount)), (1.0 * 1 / 12)) - 1), (1.0 * 1 / amountOfDaysInMonth)) - 1) * amount;
    }
}
