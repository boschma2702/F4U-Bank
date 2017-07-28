package com.bank.service.account.accountsaving;

import com.bank.repository.accountsaving.AccountSavingRepository;
import com.bank.util.Logging.Logger;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountSavingResetService extends DayPassedListener {

    @Autowired
    private AccountSavingRepository accountSavingRepository;

    @Override
    public void onDayPassed(Date start, Date end) {
        Logger.info("Resetting minimum day amounts of saving accounts");
        accountSavingRepository.resetMinimumDayAmount();
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
