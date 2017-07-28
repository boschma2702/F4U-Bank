package com.bank.service.interest;

import com.bank.bean.account.AccountBean;
import com.bank.repository.account.AccountRepository;
import com.bank.service.transaction.TransactionCreateService;
import com.bank.service.transaction.TransactionService;
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

    @Autowired
    private TransactionService transactionService;

    public void transferOverdraftInterest(){
        Logger.info("Transferring overdraft interest");
        List<AccountBean> accountBeans = accountRepository.getActiveAccountBeansWithOverdraftInterest();
        for(AccountBean accountBean : accountBeans){
            BigDecimal amount = AmountFormatter.format(accountBean.getBuildUpOverdraftInterest()).negate();
            if(amount.compareTo(BigDecimal.ZERO)>0){
                transactionService.retrieveTransaction(accountBean, amount, "Overdraft interest");
            }
            Logger.info("Overdraft interest amount=%s transferred from accountId=%s", amount, accountBean.getAccountId());
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
