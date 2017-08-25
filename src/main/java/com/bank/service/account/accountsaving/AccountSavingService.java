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
public class AccountSavingService {

    @Autowired
    private AccountSavingRepository accountSavingRepository;

    @Autowired
    private AccountService accountService;

    public AccountSavingBean getAccountSavingBeanByAccountId(int accountId) throws InvalidParamValueException {
        Logger.info("Retrieving AccountSavingBean of accountId=%s", accountId);
        AccountBean accountBean = accountService.getAccountBeanByAccountId(accountId);
        return getAccountSavingsBeanByAccountBean(accountBean);
    }

    public AccountSavingBean getAccountSavingsBeanByAccountBean(AccountBean accountBean) throws InvalidParamValueException {
        Logger.info("Retrieving AccountSavingBean of accountBeanId=%s", accountBean.getAccountId());
        AccountSavingBean accountSavingBean = accountSavingRepository.findAccountSavingBeanByAccountBean(accountBean);
        if (accountSavingBean == null) {
            Logger.error("Could not retrieve AccountSavingBean of accountId=%s", accountBean.getAccountId());
            throw new InvalidParamValueException("Savings account not known of given account");
        }
        return accountSavingBean;
    }

}
