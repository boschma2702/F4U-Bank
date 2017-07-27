package com.bank.service.interest;

import com.bank.bean.account.AccountBean;
import com.bank.repository.account.AccountRepository;
import com.bank.util.AmountFormatter;
import com.bank.util.Logging.Logger;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class InterestOverdraftTransferService extends DayPassedListener {

    @Autowired
    private AccountRepository accountRepository;

    public void transferOverdraftInterest(){
        Logger.info("Transferring overdraft interest");
        List<AccountBean> accountBeans = accountRepository.getActiveAccountBeansWithOverdraftInterest();
        for(AccountBean accountBean : accountBeans){
            double amount = AmountFormatter.format(accountBean.getBuildUpOverdraftInterest());
            accountBean.setAmount(accountBean.getAmount()+amount);
            accountBean.setBuildUpOverdraftInterest(0);
            //TODO maybe change transferring interest a transaction
            Logger.info("Overdraft interest amount=%s transferred to accountId=%s", amount, accountBean.getAccountId());
            accountRepository.save(accountBean);
        }
    }

    @Override
    public void onDayPassed(Date start, Date end) {
        //If last day of month, then transfer interest
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int amountOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(calendar.get(Calendar.DAY_OF_MONTH)==amountOfDaysInMonth){
            transferOverdraftInterest();
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
