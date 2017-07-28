package com.bank.service.overdraft;

import com.bank.repository.account.AccountRepository;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OverdraftInterestResetService extends DayPassedListener {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void onDayPassed(Date start, Date end) {
        accountRepository.resetMinimumDayAmount();
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
