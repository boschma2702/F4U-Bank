package com.bank.service.account.accountsaving;

import com.bank.bean.account.AccountBean;
import com.bank.bean.acountsavings.AccountSavingBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.accountsaving.AccountSavingRepository;
import com.bank.service.account.AccountService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountSavingOpenService {

    @Autowired
    private AccountSavingService accountSavingService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountSavingRepository accountSavingRepository;

    public void openSavingsAccount(String accountNumber) throws InvalidParamValueException {
        Logger.info("Opening savings account for accountNumber=%s", accountNumber);
        AccountBean accountBean = accountService.getAccountBeanByAccountNumber(accountNumber);
        try{
            accountSavingService.getAccountSavingsBeanByAccountBean(accountBean);
        } catch (InvalidParamValueException e) {
            AccountSavingBean accountSavingBean = new AccountSavingBean();
            accountSavingBean.setAccountBean(accountBean);
            accountSavingRepository.save(accountSavingBean);
            return;
        }
        Logger.error("Could not open savings account for accountNumber=%s, savings account already opened", accountNumber);
        throw new InvalidParamValueException("Savings account already opened");
    }

}
