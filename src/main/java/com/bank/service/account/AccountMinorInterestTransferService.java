package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.account.AccountRepository;
import com.bank.service.transaction.TransactionService;
import com.bank.util.AmountFormatter;
import com.bank.util.logging.Logger;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AccountMinorInterestTransferService extends DayPassedListener {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public void onDayPassed(Date start, Date end) {
        //If last day of month, then transfer overdraft
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        if (calendar.get(Calendar.MONTH) == 0 && calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            transferInterest();
        }
    }

    private void transferInterest() {
        Logger.info("Transferring interest of minor accounts");
        List<AccountBean> accountBeans = accountRepository.getAllActiveMinorAccounts();
        for(AccountBean accountBean : accountBeans){
            BigDecimal bigDecimal = AmountFormatter.format(accountBean.getBuildUpInterest());
            if(bigDecimal.compareTo(BigDecimal.ZERO) > 0){
                try {
                    transactionService.doSingleTransaction(accountBean, null, bigDecimal, "Interest of past year");
                } catch (InvalidParamValueException e) {
                    // do nothing
                }
            }
        }
        accountRepository.resetBuildUpInterest();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
