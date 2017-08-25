package com.bank.service.customeraccount;

import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.repository.customeraccount.CustomerAccountRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerAccountService {
    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    public void addCustomerAccount(CustomerAccount customerAccount) throws NoEffectException {
        Logger.info("Adding CustomerAccount with customerId=%s and accountId=%s", customerAccount.getCustomerId(), customerAccount.getAccountId());
        if (customerAccountRepository.getFirstByAccountIdAndCustomerId(customerAccount.getAccountId(), customerAccount.getCustomerId()) == null) {
            customerAccountRepository.save(customerAccount);
        } else {
            Logger.error("Could not add CustomerAccount, customerId=%s is already assigned to accountId=%s", customerAccount.getCustomerId(), customerAccount.getAccountId());
            throw new NoEffectException("Customer already assigned to this account");
        }
    }

    public void removeCustomerAccount(int customerId, int accountId) throws NoEffectException {
        Logger.info("Removing CustomerAccount with customerId=%s and accountId=%s", customerId, accountId);
        if (customerAccountRepository.getFirstByAccountIdAndCustomerId(accountId, customerId) != null) {
            customerAccountRepository.deleteByCustomerIdAndAccountId(customerId, accountId);
        } else {
            Logger.error("Could not remove CustomerAccount, customerId=%s not assigned to accountId=%s", customerId, accountId);
            throw new NoEffectException("Customer not assigned to this account");
        }
    }

    public CustomerAccount getCustomerAccountByAccountIdAndCustomerId(int accountId, int customerId) throws InvalidParamValueException {
        CustomerAccount customerAccount = customerAccountRepository.getFirstByAccountIdAndCustomerId(accountId, customerId);
        if (customerAccount == null) {
            Logger.error("Could not retrieve customerAccount of accountId=%s and customerId=%s", accountId, customerId);
            throw new InvalidParamValueException("Could not find customer");
        }
        return customerAccount;
    }

    public CustomerAccount getMainCustomerAccountOfAccountId(int accountId) throws InvalidParamValueException {
        CustomerAccount customerAccount = customerAccountRepository.getMainCustomerAccount(accountId);
        if (customerAccount == null) {
            Logger.error("Could not retrieve main customerAccount of accountId=%s", accountId);
            throw new InvalidParamValueException("Could not find main customer");
        }
        return customerAccount;
    }


}
