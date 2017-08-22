package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.bean.acountsavings.AccountSavingBean;
import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.account.AccountAmountProjection;
import com.bank.service.account.accountsaving.AccountSavingService;
import com.bank.service.creditcard.CreditCardService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountAmountService {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountSavingService accountSavingService;

    @Autowired
    private CreditCardService creditCardService;

    public AccountAmountProjection getBalance(int accountId) throws InvalidParamValueException {
        Logger.info("Getting balance of accountId:%s", accountId);
        AccountBean accountBean = accountService.getAccountBeanByAccountId(accountId);
        AccountAmountProjection projection = new AccountAmountProjection();
        projection.setBalance(accountBean.getAmount().doubleValue());
        try {
            AccountSavingBean accountSavingBean = accountSavingService.getAccountSavingsBeanByAccountBean(accountBean);
            projection.setSavingAccountBalance(accountSavingBean.getAmount().doubleValue());
        } catch (InvalidParamValueException e) {
            // do nothing
        }
        try {
            CreditCardBean creditCardBean = creditCardService.getCreditCardBeanByAccountId(accountId, true);
            double amount = creditCardBean.getCredit().compareTo(BigDecimal.ZERO) > 0 ? creditCardBean.getCredit().doubleValue() : 0;
            projection.setCredit(amount);
        }catch (InvalidParamValueException e) {
            // do nothing, leave credit value null
        }
        return projection;
    }
}
