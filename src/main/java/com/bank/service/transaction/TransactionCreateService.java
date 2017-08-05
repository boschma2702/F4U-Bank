package com.bank.service.transaction;

import com.bank.bean.account.AccountBean;
import com.bank.bean.card.CardBean;
import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.InvalidPINException;
import com.bank.exception.InvalidParamValueException;
import com.bank.service.account.AccountService;
import com.bank.service.card.CardValidateService;
import com.bank.service.creditcard.CreditCardService;
import com.bank.service.creditcard.CreditCardValidateService;
import com.bank.util.CreditCardNumberChecker;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionCreateService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardValidateService cardValidateService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CreditCardValidateService creditCardValidateService;

    @Autowired
    private TransactionCreditCardService transactionCreditCardService;

    public void depositIntoAccount(int accountId, String pinCard, String pinCode, BigDecimal amount) throws InvalidPINException, InvalidParamValueException {
        AccountBean accountBean = accountService.getAccountBeanByAccountId(accountId);
        CardBean cardBean = cardValidateService.validateCard(accountBean.getAccountId(), pinCard, pinCode);
        transactionService.doSingleTransaction(accountBean, cardBean, amount);
    }

    public void payFromAccount(int sourceId, int targetId, String pinCard, String pinCode, BigDecimal amount) throws InvalidParamValueException, InvalidPINException {
        AccountBean sourceAccountBean = accountService.getAccountBeanByAccountId(sourceId);
        AccountBean targetAccountBean = accountService.getAccountBeanByAccountId(targetId);
        if(CreditCardNumberChecker.isCreditCardNumber(pinCard)){
            CreditCardBean creditCardBean = creditCardValidateService.validateCreditCard(pinCard, pinCode);
            //maybe check that sourceIBAN is same as creditCardBean
            transactionCreditCardService.doTransaction(creditCardBean, targetAccountBean, amount);
        }else{
            CardBean cardBean = cardValidateService.validateCard(sourceAccountBean.getAccountId(), pinCard, pinCode);
            transactionService.doTransaction(sourceAccountBean, targetAccountBean, amount, cardBean, "", "");
        }
    }

    public void transferMoney(String sourceIBAN, String targetIBAN, String targetName, BigDecimal amount, String description) throws InvalidParamValueException {
        AccountBean sourceAccountBean = accountService.getAccountBeanByAccountNumber(sourceIBAN);
        AccountBean targetAccountBean = accountService.getAccountBeanByAccountNumber(targetIBAN);

        transactionService.doTransaction(sourceAccountBean, targetAccountBean, amount, null, description, targetName);
    }

}
