package com.bank.service.transaction;

import com.bank.bean.account.AccountBean;
import com.bank.bean.acountsavings.AccountSavingBean;
import com.bank.bean.transaction.TransactionBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.transaction.TransactionRepository;
import com.bank.service.account.AccountUpdateAmountService;
import com.bank.service.account.accountsaving.AccountSavingService;
import com.bank.service.account.accountsaving.AccountSavingUpdateAmountService;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionSavingsService {

    @Autowired
    private AccountSavingService accountSavingService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountUpdateAmountService accountUpdateAmountService;

    @Autowired
    private AccountSavingUpdateAmountService accountSavingUpdateAmountService;

    @Transactional
    public void doSavingsTransaction(AccountBean accountBean, boolean fromSavings, BigDecimal amount, String targetName, String description) throws InvalidParamValueException {
        Logger.info("Transaction fromSavings=%s of accountBeanId=%s", fromSavings, accountBean.getAccountId());
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            Logger.error("Transaction fromSavings=%s of accountBeanId=%s has invalid amount=%s", fromSavings, accountBean.getAccountId(), amount);
            throw new InvalidParamValueException("Invalid Amount");
        }
        AccountSavingBean accountSavingBean = accountSavingService.getAccountSavingsBeanByAccountBean(accountBean);
        //Check if transaction is allowed
        if (!fromSavings) {
            BigDecimal newSourceAmount = accountBean.getAmount().subtract(amount);
            if (!(newSourceAmount.compareTo(BigDecimal.ZERO) >= 0 || newSourceAmount.compareTo(new BigDecimal(-accountBean.getOverdraftLimit())) >= 0)) {
                Logger.error("Could not make transaction fromSavings=%s of accountBeanId=%s, sourceaccount overdraft to high", fromSavings, accountBean.getAccountId());
                throw new InvalidParamValueException("Source account overdraft to high");
            }
        } else {
            BigDecimal newSourceAmount = accountBean.getAmount().subtract(amount);
            if (newSourceAmount.compareTo(BigDecimal.ZERO) < 0) {
                Logger.error("Could not make transaction fromSavings=%s of accountBeanId=%s, transaction would result negative saving balance", fromSavings, accountBean.getAccountId());
                throw new InvalidParamValueException("Savings account not enough funds");
            }
        }

        TransactionBean transaction = new TransactionBean();
        transaction.setSourceBean(accountBean);
        transaction.setTargetBean(accountBean);
        transaction.setFromSavings(fromSavings);
        transaction.setTargetName(targetName);
        transaction.setAmount(amount);
        transaction.setComment(description);

        transactionRepository.save(transaction);
        BigDecimal accountAmount = fromSavings ? amount : amount.negate();
        BigDecimal accountSavingAmount = fromSavings ? amount.negate() : amount;
        accountUpdateAmountService.updateAmount(accountBean.getAccountId(), accountAmount);
        accountSavingUpdateAmountService.updateAmount(accountBean.getAccountId(), accountSavingAmount);
    }

    @Transactional
    public void doToSavingsTransaction(AccountBean accountBean, BigDecimal amount, String description) throws InvalidParamValueException {
        Logger.info("Transaction fromSavings=%s of accountBeanId=%s", false, accountBean.getAccountId());
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            Logger.error("Transaction fromSavings=%s of accountBeanId=%s has invalid amount=%s", false, accountBean.getAccountId(), amount);
            throw new InvalidParamValueException("Invalid Amount");
        }

        TransactionBean transactionBean = new TransactionBean();
        transactionBean.setTargetBean(accountBean);
        transactionBean.setAmount(amount);
        transactionBean.setComment(description);

        transactionRepository.save(transactionBean);

        accountSavingUpdateAmountService.updateAmount(accountBean.getAccountId(), amount);
    }

}
