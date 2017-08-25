package com.bank.service.account.accountsaving;

import com.bank.bean.acountsavings.AccountSavingBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.accountsaving.AccountSavingRepository;
import com.bank.service.transaction.TransactionSavingsService;
import com.bank.util.AmountFormatter;
import com.bank.util.logging.Logger;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

@Service
public class AccountSavingInterestTransferService extends DayPassedListener {

    @Autowired
    private AccountSavingRepository accountSavingRepository;

    @Autowired
    private TransactionSavingsService transactionSavingsService;


    @Override
    public void onDayPassed(Date start, Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        //If first of January, then transfer build up overdraft
        if (calendar.get(Calendar.MONTH) == 0 && calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            transferAccountSavingInterest();
        }
    }

    private void transferAccountSavingInterest() {
        Logger.info("Transferring overdraft of savings account");
        Iterator<AccountSavingBean> iterator = accountSavingRepository.findAll().iterator();
        while (iterator.hasNext()) {
            AccountSavingBean accountSavingBean = iterator.next();
            String description = "Interest of past year";
            BigDecimal amount = AmountFormatter.format(accountSavingBean.getBuildUpInterest());
            try {
                transactionSavingsService.doToSavingsTransaction(accountSavingBean.getAccountBean(), amount, description);
                accountSavingBean.setBuildUpInterest(0);
            } catch (InvalidParamValueException e) {
                //Interest is always positive and can not happen
            }
        }
        accountSavingRepository.resetBuildUpInterest();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
