package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.account.AccountRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountTransferLimitService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    public void setTransferLimit(int accountId, BigDecimal transferLimit) throws InvalidParamValueException, AccountFrozenException {
        Logger.info("Setting transfer limit of accountId=%s", accountId);
        if (transferLimit.compareTo(BigDecimal.ZERO) < 0) {
            Logger.error("Could not set transfer limit of accountId=%s, invalid transfer limit", accountId);
            throw new InvalidParamValueException("Invalid transferLimit");
        }
        AccountBean accountBean = accountService.getAccountBeanByAccountIdCheckOnFrozen(accountId);
        accountBean.setTransferLimit(transferLimit);
        accountRepository.save(accountBean);
    }

}
