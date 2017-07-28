package com.bank.service.account.accountsaving;

import com.bank.bean.account.AccountBean;
import com.bank.bean.acountsavings.AccountSavingBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.accountsaving.AccountSavingRepository;
import com.bank.service.account.AccountService;
import com.bank.service.transaction.TransactionSavingsService;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountSavingCloseService {

    @Autowired
    private TransactionSavingsService transactionSavingsService;

    @Autowired
    private AccountSavingService accountSavingService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountSavingRepository accountSavingRepository;

    @Transactional
    public void closeAccount(String iBAN) throws InvalidParamValueException {
        Logger.info("Closing saving account of iBAN=%s", iBAN);
        AccountBean accountBean = accountService.getAccountBeanByAccountNumber(iBAN);
        AccountSavingBean accountSavingBean = accountSavingService.getAccountSavingsBeanByAccountBean(accountBean);
        String description = "Closing of saving account";
        if (accountSavingBean.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            transactionSavingsService.doSavingsTransaction(accountBean, true, accountSavingBean.getAmount(), "", description);
        }
        accountSavingRepository.delete(accountSavingBean);
    }

}
