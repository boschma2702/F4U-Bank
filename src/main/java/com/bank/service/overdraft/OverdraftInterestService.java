package com.bank.service.overdraft;

import com.bank.bean.account.AccountBean;
import com.bank.repository.account.AccountRepository;
import com.bank.util.InterestCalculator;
import com.bank.util.time.DayPassedListener;
import com.bank.util.logging.Logger;
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
            double interest = InterestCalculator.getInterest(amountOfDaysInMonth, accountBean.getMinimumDayAmount(), MONTHLY_OVERDRAFT_INTEREST);
            Logger.info("Overdraft overdraft of accountId=%s is overdraft=%s", accountBean.getAccountId(), interest);
            accountBean.setBuildUpOverdraftInterest(buildUpInterest+interest);
            accountRepository.save(accountBean);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
