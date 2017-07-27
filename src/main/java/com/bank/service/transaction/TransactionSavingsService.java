package com.bank.service.transaction;

import com.bank.bean.account.AccountBean;
import com.bank.bean.acountsavings.AccountSavingBean;
import com.bank.bean.transaction.TransactionBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.transaction.TransactionRepository;
import com.bank.service.account.AccountUpdateAmountService;
import com.bank.service.accountsaving.AccountSavingService;
import com.bank.service.accountsaving.AccountSavingUpdateAmountService;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void doSavingsTransaction(AccountBean accountBean, boolean fromSavings, double amount, String targetName, String description) throws InvalidParamValueException {
        Logger.info("Transaction fromSavings=%s of accountBeanId=%s", fromSavings, accountBean.getAccountId());
        if (amount <= 0) {
            Logger.error("Transaction fromSavings=%s of accountBeanId=%s has invalid amount=%s", fromSavings, accountBean.getAccountId(), amount);
            throw new InvalidParamValueException("Invalid Amount");
        }
        AccountSavingBean accountSavingBean = accountSavingService.getAccountSavingsBeanByAccountBean(accountBean);
        //Check if transaction is allowed
        if (!fromSavings) {
            double newSourceAmount = accountBean.getAmount() - amount;
            if (!(newSourceAmount >= 0 || newSourceAmount >= -accountBean.getOverdraftLimit())) {
                Logger.error("Could not make transaction fromSavings=%s of accountBeanId=%s, sourceaccount overdraft to high", fromSavings, accountBean.getAccountId());
                throw new InvalidParamValueException("Source account overdraft to high");
            }
        } else {
            double newSourceAmount = accountSavingBean.getAmount() - amount;
            if (newSourceAmount < 0) {
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
        double accountAmount = fromSavings ? amount : -amount;
        double accountSavingAmount = fromSavings ? -amount : amount;
        accountUpdateAmountService.updateAmount(accountBean.getAccountId(), accountAmount);
        accountSavingUpdateAmountService.updateAmount(accountBean.getAccountId(), accountSavingAmount);
    }

    @Transactional
    public void doToSavingsTransaction(AccountBean accountBean, double amount, String description) throws InvalidParamValueException {
        Logger.info("Transaction fromSavings=%s of accountBeanId=%s", false, accountBean.getAccountId());
        if (amount <= 0) {
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