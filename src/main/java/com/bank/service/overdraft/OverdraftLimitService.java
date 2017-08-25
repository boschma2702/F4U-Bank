package com.bank.service.overdraft;

import com.bank.bean.account.AccountBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.account.AccountOverdraftLimitProjection;
import com.bank.repository.account.AccountRepository;
import com.bank.service.account.AccountService;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.bank.util.systemvariable.SystemVariableNames.MAX_OVERDRAFT_LIMIT;

@Service
public class OverdraftLimitService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    public void setOverdraft(String accountNumber, double amount) throws InvalidParamValueException {
        Logger.info("Setting overdraft limit of accountNumber=%s to amount=%s", accountNumber, amount);
        if(!(amount>=0 && amount<= (Double) systemVariableRetrieveService.getObjectInternally(MAX_OVERDRAFT_LIMIT))){
            Logger.error("Incorrect amount given, amount=%s", amount);
            throw new InvalidParamValueException("invalid overdraft amount");
        }

        AccountBean accountBean = accountService.getAccountBeanByAccountNumber(accountNumber);
        accountBean.setOverdraftLimit(amount);
        accountRepository.save(accountBean);
    }

    public AccountOverdraftLimitProjection getOverdraft(String accountNumber) throws InvalidParamValueException {
        Logger.info("Retrieving overdraft limit of accountNumber=%s", accountNumber);
        AccountBean accountBean = accountService.getAccountBeanByAccountNumber(accountNumber);
        AccountOverdraftLimitProjection overdraftProjection = new AccountOverdraftLimitProjection();
        overdraftProjection.setOverdraftLimit(accountBean.getOverdraftLimit());
        return overdraftProjection;
    }

}
