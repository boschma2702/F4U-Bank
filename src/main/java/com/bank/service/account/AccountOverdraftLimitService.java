package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.account.AccountOverdraftLimitProjection;
import com.bank.repository.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountOverdraftLimitService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    public void setOverdraft(String accountNumber, double amount) throws InvalidParamValueException {
        AccountBean accountBean = accountService.getAccountBeanByAccountNumber(accountNumber);
        accountBean.setOverdraftLimit(amount);
        accountRepository.save(accountBean);
    }

    public AccountOverdraftLimitProjection getOverdraft(String accountNumber) throws InvalidParamValueException {
        AccountBean accountBean = accountService.getAccountBeanByAccountNumber(accountNumber);
        AccountOverdraftLimitProjection overdraftProjection = new AccountOverdraftLimitProjection();
        overdraftProjection.setOverdraftLimit(accountBean.getOverdraftLimit());
        return overdraftProjection;
    }

}
