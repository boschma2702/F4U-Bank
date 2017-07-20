package com.bank.service.interest;

import com.bank.bean.account.AccountBean;
import com.bank.repository.account.AccountRepository;
import com.bank.service.time.TimeService;
import com.bank.util.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class InterestOverdraftService implements DayPassedListener {

    private final static double ANNUAL_OVERDRAFT_INTEREST = 0.10;
    private final static double MONTHLY_OVERDRAFT_INTEREST = Math.pow((1+ANNUAL_OVERDRAFT_INTEREST), (1.0*1/12)) - 1;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InterestOverdraftTransferService interestOverdraftTransferService;

    public InterestOverdraftService() {
        TimeService.TIMESIMULATOR.registerDayPassedListener(this);
    }

    @Override
    @Transactional
    public void onDayPassed(Date start, Date end) {
        System.out.println("day passed");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int amountOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<AccountBean> accountBeans = accountRepository.getActiveAccountBeansWithNegativeDayOverdraft();
        for(AccountBean accountBean : accountBeans){
            double buildUpInterest = accountBean.getBuildUpOverdraftInterest();
            double interest = getInterest(amountOfDaysInMonth, accountBean.getMinimumDayAmount());
            accountBean.setBuildUpOverdraftInterest(buildUpInterest+interest);
            accountRepository.save(accountBean);
        }
        accountRepository.resetMinimumDayAmount();

        //If last day of month, then transfer interest
        if(calendar.get(Calendar.DAY_OF_MONTH)==amountOfDaysInMonth){
            interestOverdraftTransferService.transferOverdraftInterest();
        }
    }

    private double getInterest(int amountOfDaysInMonth, double amount){
        return (Math.pow((1 + MONTHLY_OVERDRAFT_INTEREST), (1.0*1/amountOfDaysInMonth)) - 1) * amount;
    }
}
