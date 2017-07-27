package com.bank.service.transaction;

import com.bank.bean.account.AccountBean;
import com.bank.bean.card.CardBean;
import com.bank.bean.transaction.TransactionBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.transaction.TransactionRepository;
import com.bank.service.account.AccountUpdateAmountService;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountUpdateAmountService accountUpdateAmountService;

    public void doTransaction(AccountBean sourceAccountBean, AccountBean targetAccountBean, double amount) throws InvalidParamValueException {
        doTransaction(sourceAccountBean, targetAccountBean, amount, null, "", "");
    }

    public void doTransaction(AccountBean sourceAccountBean, AccountBean targetAccountBean, double amount, CardBean card, String description, String targetName) throws InvalidParamValueException {
        Logger.info("Making transaction form sourceAccountBeanId=%s, to targetAccountBeanId=%s", sourceAccountBean.getAccountId(), targetAccountBean.getAccountId());
        if(amount <= 0){
            Logger.info("Transaction failed, invalid amount=%s", amount);
            throw new InvalidParamValueException("Invalid Amount");
        }
        if(sourceAccountBean.getAccountNumber().equals(targetAccountBean.getAccountNumber())){
            Logger.error("Transaction failed, can not transfer money to the same accountNumber");
            throw new InvalidParamValueException("Can not transfer money to same accountNumber");
        }
        double newSourceAmount = sourceAccountBean.getAmount()-amount;
        if(!(newSourceAmount >= 0 || newSourceAmount >= -sourceAccountBean.getOverdraftLimit())){
            throw new InvalidParamValueException("Source account overdraft to high");
        }

        TransactionBean transactionBean = new TransactionBean();
        transactionBean.setSourceBean(sourceAccountBean);
        transactionBean.setTargetBean(targetAccountBean);
        transactionBean.setAmount(amount);
        transactionBean.setTargetName(targetName);

        transactionBean.setCard(card);
        transactionBean.setComment(description);

        transactionRepository.save(transactionBean);

        accountUpdateAmountService.updateAmount(sourceAccountBean.getAccountId(), targetAccountBean.getAccountId(), amount);
    }

    public void doSingleTransaction(AccountBean targetAccountBean, CardBean card, double amount) throws InvalidParamValueException {
        if(amount <= 0){
            throw new InvalidParamValueException("Invalid Amount");
        }

        TransactionBean transactionBean = new TransactionBean();
        transactionBean.setTargetBean(targetAccountBean);
        transactionBean.setAmount(amount);

        transactionBean.setCard(card);
        transactionBean.setComment("");

        transactionRepository.save(transactionBean);

        accountUpdateAmountService.updateAmount(targetAccountBean.getAccountId(), amount);
    }


}
