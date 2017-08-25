package com.bank.service.overdraft;

import com.bank.bean.account.AccountBean;
import com.bank.repository.account.AccountRepository;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.util.InterestCalculator;
import com.bank.util.logging.Logger;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bank.util.systemvariable.SystemVariableNames.OVERDRAFT_INTEREST_RATE;

@Service
public class OverdraftInterestService extends DayPassedListener {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    @Override
    @Transactional
    public void onDayPassed(Date start, Date end) {
        Logger.info("Calculating overdraft overdraft over day");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int amountOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<AccountBean> accountBeans = accountRepository.getActiveAccountBeansWithNegativeDayOverdraft();
        for (AccountBean accountBean : accountBeans) {
            double buildUpInterest = accountBean.getBuildUpOverdraftInterest();
            double interest = InterestCalculator.getInterest(amountOfDaysInMonth, accountBean.getMinimumDayAmount(), (double) systemVariableRetrieveService.getObjectInternally(OVERDRAFT_INTEREST_RATE));
            Logger.info("Overdraft overdraft of accountId=%s is overdraft=%s", accountBean.getAccountId(), interest);
            accountBean.setBuildUpOverdraftInterest(buildUpInterest + interest);
            accountRepository.save(accountBean);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
