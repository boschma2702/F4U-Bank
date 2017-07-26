package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.account.AccountAmountProjection;
import com.bank.repository.account.AccountRepository;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountAmountService {
    @Autowired
    private AccountService accountService;

    public AccountAmountProjection getBalance(int accountId) throws InvalidParamValueException {
        Logger.info("Getting balance of accountId:%s",accountId);
        AccountBean accountBean = accountService.getAccountBeanByAccountId(accountId);
        AccountAmountProjection projection = new AccountAmountProjection();
        projection.setBalance(accountBean.getAmount());
        return projection;
    }
}
