package com.bank.service.transaction;

import com.bank.bean.account.AccountBean;
import com.bank.bean.creditcard.CreditCardBean;
import com.bank.bean.transaction.TransactionBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.transaction.TransactionRepository;
import com.bank.service.account.AccountUpdateAmountService;
import com.bank.service.creditcard.CreditCardUpdateAmountService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionCreditCardService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreditCardUpdateAmountService creditCardUpdateAmountService;

    @Autowired
    private AccountUpdateAmountService accountUpdateAmountService;

    @Transactional
    public void doTransaction(CreditCardBean creditCardBean, AccountBean targetAccountBean, BigDecimal amount) throws InvalidParamValueException {
        Logger.info("Transferring amount=%s from creditCardId=%s to targetAccountId=%s", amount.doubleValue(), creditCardBean.getCreditCardId(), targetAccountBean.getAccountId());
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            Logger.error("Can not complete transaction, invalid amount=%s with creditCardId=%s", amount.doubleValue(), creditCardBean.getCreditCardId());
        }
        if (creditCardBean.getCredit().compareTo(amount) < 0) {
            Logger.error("Can not complete transaction, creditcardlimit of creditCardNumber=%s is not sufficient to pay amount=%s", creditCardBean.getCreditCardNumber(), amount.doubleValue());
            throw new InvalidParamValueException("Credit card limit reached");
        }
        TransactionBean transactionBean = new TransactionBean();
        transactionBean.setCreditCardBean(creditCardBean);
        transactionBean.setTargetBean(targetAccountBean);
        transactionBean.setAmount(amount);

        transactionRepository.save(transactionBean);

        creditCardUpdateAmountService.updateAmount(creditCardBean.getCreditCardId(), amount.negate());
        accountUpdateAmountService.updateAmount(targetAccountBean.getAccountId(), amount);

    }

}
