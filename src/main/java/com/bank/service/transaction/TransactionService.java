package com.bank.service.transaction;

import com.bank.bean.account.AccountBean;
import com.bank.bean.card.CardBean;
import com.bank.bean.transaction.TransactionBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.transaction.TransactionRepository;
import com.bank.service.account.AccountUpdateAmountService;
import com.bank.service.card.CardSubtractDayRemainingService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountUpdateAmountService accountUpdateAmountService;

    @Autowired
    private CardSubtractDayRemainingService cardSubtractDayRemainingService;

    public void doTransaction(AccountBean sourceAccountBean, AccountBean targetAccountBean, BigDecimal amount) throws InvalidParamValueException {
        doTransaction(sourceAccountBean, targetAccountBean, amount, null, "", "");
    }

    public void doTransaction(AccountBean sourceAccountBean, AccountBean targetAccountBean, BigDecimal amount, CardBean card, String description, String targetName) throws InvalidParamValueException {
        Logger.info("Making transaction form sourceAccountBeanId=%s, to targetAccountBeanId=%s", sourceAccountBean.getAccountId(), targetAccountBean.getAccountId());
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            Logger.info("Transaction failed, invalid amount=%s", amount);
            throw new InvalidParamValueException("Invalid Amount");
        }
        if(sourceAccountBean.getAccountNumber().equals(targetAccountBean.getAccountNumber())){
            Logger.error("Transaction failed, can not transfer money to the same accountNumber");
            throw new InvalidParamValueException("Can not transfer money to same accountNumber");
        }
        BigDecimal newSourceAmount = sourceAccountBean.getAmount().subtract(amount.negate());
        if(!(newSourceAmount.compareTo(BigDecimal.ZERO) >= 0 || newSourceAmount.compareTo(new BigDecimal(-sourceAccountBean.getOverdraftLimit())) >= 0)){
            throw new InvalidParamValueException("Source account overdraft to high");
        }

        if(card != null){
            cardSubtractDayRemainingService.subtractDayRemaining(card, amount);
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

    public void doSingleTransaction(AccountBean targetAccountBean, CardBean card, BigDecimal amount) throws InvalidParamValueException {
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
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

    /**
     * Retrieves money form an account and sends it to 'nowhere'. Does not check if account it is allowed to retrieve
     * the amount.
     * @param sourceAccountBean
     * @param amount to retrieve, must be positive
     * @param comment
     */
    public void retrieveTransaction(AccountBean sourceAccountBean, BigDecimal amount, String comment){
        TransactionBean transactionBean = new TransactionBean();
        transactionBean.setSourceBean(sourceAccountBean);
        transactionBean.setAmount(amount);
        transactionBean.setComment(comment);

        transactionRepository.save(transactionBean);

        accountUpdateAmountService.updateAmount(sourceAccountBean.getAccountId(), amount.negate());
    }


}
