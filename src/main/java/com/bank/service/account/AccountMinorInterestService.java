package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.repository.account.AccountRepository;
import com.bank.util.InterestCalculator;
import com.bank.util.logging.Logger;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AccountMinorInterestService extends DayPassedListener {

    private final static double ANNUAL_OVERDRAFT_INTEREST = 0.02017;
    private final static double MONTHLY_OVERDRAFT_INTEREST = ANNUAL_OVERDRAFT_INTEREST/12;

    private final static double MAXIMUM_ACCOUNT_INTEREST_AMOUNT = 2500;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void onDayPassed(Date start, Date end) {
        Logger.info("Calculating interest over minor accounts");
        Calendar day = Calendar.getInstance();
        day.setTime(start);
        int amountOfDaysInMonth = day.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<AccountBean> accountBeans = accountRepository.getAllActiveMinorAccounts();
        for(AccountBean accountBean : accountBeans){
            double buildUpInterest = accountBean.getBuildUpInterest();
            double amount = accountBean.getMinimumDayAmount() > MAXIMUM_ACCOUNT_INTEREST_AMOUNT ? MAXIMUM_ACCOUNT_INTEREST_AMOUNT : accountBean.getMinimumDayAmount();
            double interest = InterestCalculator.getInterest(amountOfDaysInMonth, amount, MONTHLY_OVERDRAFT_INTEREST);
            accountBean.setBuildUpInterest(buildUpInterest + interest);
            accountRepository.save(accountBean);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
