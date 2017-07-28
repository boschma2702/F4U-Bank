package com.bank.service.overdraft;

import com.bank.bean.account.AccountBean;
import com.bank.repository.account.AccountRepository;
import com.bank.util.time.DayPassedListener;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OverdraftInterestService extends DayPassedListener {

    private final static double ANNUAL_OVERDRAFT_INTEREST = 0.10;
    private final static double MONTHLY_OVERDRAFT_INTEREST = Math.pow((1+ANNUAL_OVERDRAFT_INTEREST), (1.0*1/12)) - 1;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    @Transactional
    public void onDayPassed(Date start, Date end) {
        Logger.info("Calculating overdraft overdraft over day");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int amountOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<AccountBean> accountBeans = accountRepository.getActiveAccountBeansWithNegativeDayOverdraft();
        for(AccountBean accountBean : accountBeans){
            double buildUpInterest = accountBean.getBuildUpOverdraftInterest();
            double interest = getInterest(amountOfDaysInMonth, accountBean.getMinimumDayAmount());
            Logger.info("Overdraft overdraft of accountId=%s is overdraft=%s", accountBean.getAccountId(), interest);
            accountBean.setBuildUpOverdraftInterest(buildUpInterest+interest);
            accountRepository.save(accountBean);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private double getInterest(int amountOfDaysInMonth, double amount){
        return (Math.pow((1 + MONTHLY_OVERDRAFT_INTEREST), (1.0*1/amountOfDaysInMonth)) - 1) * amount;
    }
}
