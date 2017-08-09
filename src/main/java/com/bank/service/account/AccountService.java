package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NotAuthorizedException;
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

    public void checkMinor(String accountNumber) throws NotAuthorizedException {
        AccountBean accountBean = accountRepository.findAccountBeanByAccountNumber(accountNumber);
        if(accountBean.isMinorAccount()){
            Logger.error("Minor account=%s tried to execute non allowed method", accountNumber);
            throw new NotAuthorizedException("Child account is not allowed to perform this method");
        }
    }

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

    public boolean checkIfIsMainAccountHolderCheckFrozen(String accountNumber, int customerId) throws InvalidParamValueException, AccountFrozenException {
        Logger.info("Checking if customerId=%s is main accountholder of accountNumber=%s", customerId, accountNumber);
        AccountBean accountBean = accountRepository.findAccountBeanByAccountNumber(accountNumber);
        if (accountBean == null) {
            Logger.error("Unknown accountNumber=%s", accountNumber);
            throw new InvalidParamValueException("Unknown account number");
        }
        if (accountBean.isFrozen()){
            Logger.error("Could not retrieve accountBean of accountNumber=%s, request of customerId=%s", accountBean, customerId);
            throw new AccountFrozenException("Account is frozen");
        }
        CustomerAccount customerAccount = customerAccountRepository.getFirstByAccountIdAndCustomerId(accountBean.getAccountId(), customerId);
        return customerAccount != null && customerAccount.isMain();
    }

    public boolean checkIfAccountHolderCheckFrozen(String accountNumber, int customerId) throws InvalidParamValueException, AccountFrozenException {
        Logger.info("Checking if customerId=%s is accountholder of accountNumber=%s", customerId, accountNumber);
        AccountBean accountBean = accountRepository.findAccountBeanByAccountNumber(accountNumber);
        if (accountBean == null) {
            Logger.error("Unknown accountNumber=%s", accountNumber);
            throw new InvalidParamValueException("Unknown account number");
        }
        if (accountBean.isFrozen()){
            Logger.error("Could not retrieve accountBean of accountNumber=%s, request of customerId=%s", accountBean, customerId);
            throw new AccountFrozenException("Account is frozen");
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

    public AccountBean getAccountBeanByAccountIdCheckOnFrozen(int accountId) throws AccountFrozenException, InvalidParamValueException {
        Logger.info("Retrieving accountBean of accountId=%s", accountId);
        AccountBean bean = accountRepository.findAccountBeansByAccountId(accountId);
        if (bean == null) {
            Logger.error("Unknown accountId=%s", accountId);
            throw new InvalidParamValueException("Unknown account number");
        }
        if (bean.isFrozen()){
            Logger.error("Requested accountId=%s is frozen", accountId);
            throw new AccountFrozenException("Requested account is frozen");
        }
        return bean;
    }

    public AccountBean getAccountBeanByAccountNumberCheckFrozen(String accountNumber) throws InvalidParamValueException, AccountFrozenException {
        Logger.info("Retrieving accountBean of accountNumber=%s", accountNumber);
        AccountBean bean = accountRepository.findAccountBeanByAccountNumber(accountNumber);
        if (bean == null) {
            Logger.error("Unknown accountNumber=%s", accountNumber);
            throw new InvalidParamValueException("Unknown account number");
        }
        if (bean.isFrozen()){
            Logger.error("Requested accountId=%s is frozen", accountNumber);
            throw new AccountFrozenException("Requested account is frozen");
        }
        return bean;
    }
}
