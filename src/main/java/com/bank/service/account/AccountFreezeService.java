package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.repository.account.AccountRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountFreezeService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    public void freezeAccount(int accountId, boolean freeze) throws InvalidParamValueException, NoEffectException {
        Logger.info("About to freeze=%s accountId=%s", freeze, accountId);
        AccountBean accountBean = accountService.getAccountBeanByAccountId(accountId);
        if(accountBean.isFrozen() == freeze){
            Logger.info("Could not freeze=%s accountId=%s, account already (un)frozen", freeze, accountId);
            throw new NoEffectException("Account is already (un)frozen");
        }
        accountBean.setFrozen(freeze);
        accountRepository.save(accountBean);
    }

}
