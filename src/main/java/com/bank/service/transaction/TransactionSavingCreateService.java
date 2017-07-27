package com.bank.service.transaction;

import com.bank.bean.account.AccountBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.service.account.AccountService;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionSavingCreateService {

    @Autowired
    private TransactionSavingsService transactionSavingsService;

    @Autowired
    private AccountService accountService;

    public void transferMoney(String sourceIBAN, String targetIBAN, String targetName, double amount, String description) throws InvalidParamValueException {
        if(sourceIBAN.endsWith("S")&&targetIBAN.endsWith("S")){
            Logger.error("Can not transfer money, both accounts are savings account. sourceIBAN=%s, targetIBAN=%s", sourceIBAN, targetIBAN);
            throw new InvalidParamValueException("Cannot transfer money from saving account to another account");
        }
        boolean fromSavings = sourceIBAN.endsWith("S");
        String IBAN = fromSavings ? targetIBAN : sourceIBAN;
        AccountBean accountBean = accountService.getAccountBeanByAccountNumber(IBAN);
        transactionSavingsService.doSavingsTransaction(accountBean, fromSavings, amount, targetName, description);
    }
}
