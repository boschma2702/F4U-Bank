package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.account.AccountRepository;
import com.bank.repository.customeraccount.CustomerAccountRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private AccountRepository accountRepository;

    public boolean checkIfIsMainAccountHolder(String accountNumber, int customerId) throws InvalidParamValueException {
        Logger.info("Checking if customerId=%s is main accountholder of accountNumber=%s", customerId, accountNumber);
        AccountBean accountBean = accountRepository.findAccountBeanByAccountNumber(accountNumber);
        if (accountBean == null) {
            Logger.error("Unknown accountNumber=%s", accountNumber);
            throw new InvalidParamValueException("Unknown account number");
        }
        CustomerAccount customerAccount = customerAccountRepository.getFirstByAccountIdAndCustomerId(accountBean.getAccountId(), customerId);
        return customerAccount != null && customerAccount.isMain();
    }

    public boolean checkIfAccountHolder(String accountNumber, int customerId) throws InvalidParamValueException {
        Logger.info("Checking if customerId=%s is accountholder of accountNumber=%s", customerId, accountNumber);
        AccountBean accountBean = accountRepository.findAccountBeanByAccountNumber(accountNumber);
        if (accountBean == null) {
            Logger.error("Unknown accountNumber=%s", accountNumber);
            throw new InvalidParamValueException("Unknown account number");
        }
        CustomerAccount customerAccount = customerAccountRepository.getFirstByAccountIdAndCustomerId(accountBean.getAccountId(), customerId);
        return customerAccount != null;
    }

    public AccountBean getAccountBeanByAccountNumber(String accountNumber) throws InvalidParamValueException {
        Logger.info("Retrieving accountBean of accountNumber=%s", accountNumber);
        AccountBean bean = accountRepository.findAccountBeanByAccountNumber(accountNumber);
        if (bean == null) {
            Logger.error("Unknown accountNumber=%s", accountNumber);
            throw new InvalidParamValueException("Unknown account number");
        }
        return bean;
    }

    public AccountBean getAccountBeanByAccountId(int accountId) throws InvalidParamValueException {
        Logger.info("Retrieving accountBean of accountId=%s", accountId);
        AccountBean bean = accountRepository.findAccountBeansByAccountId(accountId);
        if (bean == null) {
            Logger.error("Unknown accountId=%s", accountId);
            throw new InvalidParamValueException("Unknown account number");
        }
        return bean;
    }
}
