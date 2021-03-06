package com.bank.controller;

import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidPINException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NotAuthorizedException;
import com.bank.projection.transaction.TransactionProjection;
import com.bank.service.AuthenticationService;
import com.bank.service.account.AccountService;
import com.bank.service.customer.CustomerService;
import com.bank.service.transaction.TransactionCreateService;
import com.bank.service.transaction.TransactionOverviewService;
import com.bank.service.transaction.TransactionSavingCreateService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionController {
    @Autowired
    private TransactionCreateService transactionCreateService;

    @Autowired
    private TransactionSavingCreateService transactionSavingCreateService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionOverviewService transactionOverviewService;

    @Autowired
    private CustomerService customerService;

    public void depositIntoAccount(String IBAN, String pinCard, String pinCode, BigDecimal amount) throws InvalidParamValueException, InvalidPINException, AccountFrozenException {
        transactionCreateService.depositIntoAccount(accountService.getAccountBeanByAccountNumberCheckFrozen(IBAN).getAccountId(), pinCard, pinCode, amount);
    }

    public void payFromAccount(String sourceIBAN, String targetIBAN, String pinCard, String pinCode, BigDecimal amount) throws InvalidParamValueException, InvalidPINException, AccountFrozenException {
        String iBANToCheck = sourceIBAN.endsWith("C") ? sourceIBAN.substring(0, sourceIBAN.length() - 1) : sourceIBAN;
        if (targetIBAN.endsWith("C")) {
            Logger.error("sourceIBAN=%s trying to pay to credit card", sourceIBAN);
            throw new InvalidParamValueException("Can not pay to credit card");
        }
        int sourceId = accountService.getAccountBeanByAccountNumberCheckFrozen(iBANToCheck).getAccountId();
        int targetId = accountService.getAccountBeanByAccountNumberCheckFrozen(targetIBAN).getAccountId();
        transactionCreateService.payFromAccount(sourceId, targetId, pinCard, pinCode, amount);
    }

    public void transferMoney(String authToken, String sourceIBAN, String targetIBAN, String targetName, BigDecimal amount, String description) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException {
        String iBANToCheck = sourceIBAN.endsWith("S") ? sourceIBAN.substring(0, sourceIBAN.length() - 1) : sourceIBAN;
        String iBANToCheckTarget = targetIBAN.endsWith("S") ? targetIBAN.substring(0, targetIBAN.length() - 1) : targetIBAN;
        boolean authenticated = false;
        if (AuthenticationService.instance.isCustomer(authToken)) {
            int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
            customerService.checkIfCustomerIsFrozen(customerId);
            authenticated = accountService.checkIfAccountHolder(iBANToCheck, customerId);
        } else {
            authenticated = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
        }
        if (authenticated) {
            int sourceId = accountService.getAccountBeanByAccountNumberCheckFrozen(iBANToCheck).getAccountId();
            int targetId = accountService.getAccountBeanByAccountNumberCheckFrozen(iBANToCheckTarget).getAccountId();
            if (sourceIBAN.endsWith("S") || targetIBAN.endsWith("S")) {
                transactionSavingCreateService.transferMoney(sourceIBAN, targetIBAN, targetName, amount, description);
            } else {
                transactionCreateService.transferMoney(sourceIBAN, targetIBAN, targetName, amount, description);
            }
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public List<TransactionProjection> getTransactionsOverview(String authToken, String IBAN, int nrOfTransactions) throws InvalidParamValueException, NotAuthorizedException {
        if (AuthenticationService.instance.isCustomer(authToken)) {
            int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
            if (accountService.checkIfAccountHolder(IBAN, customerId)) {
                return transactionOverviewService.getTransactionOverview(accountService.getAccountBeanByAccountNumber(IBAN).getAccountId(), nrOfTransactions);
            }
        } else {
            boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
            if (isAdministrativeEmployee) {
                return transactionOverviewService.getTransactionOverview(accountService.getAccountBeanByAccountNumber(IBAN).getAccountId(), nrOfTransactions);
            }
        }
        throw new NotAuthorizedException("Not Authorized");
    }
}