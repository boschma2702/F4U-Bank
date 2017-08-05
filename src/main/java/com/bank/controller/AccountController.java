package com.bank.controller;

import com.bank.exception.AuthenticationException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.exception.NotAuthorizedException;
import com.bank.projection.account.AccountAmountProjection;
import com.bank.projection.account.AccountOpenProjection;
import com.bank.projection.account.AccountOverdraftLimitProjection;
import com.bank.projection.customer.CustomerUsernameProjection;
import com.bank.projection.pin.PinProjection;
import com.bank.service.AuthenticationService;
import com.bank.service.account.*;
import com.bank.service.account.accountsaving.AccountSavingCloseService;
import com.bank.service.creditcard.CreditCardCloseService;
import com.bank.service.customer.CustomerService;
import com.bank.service.overdraft.OverdraftLimitService;
import com.bank.util.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountOpenService accountOpenService;

    @Autowired
    private AccountCloseService accountCloseService;

    @Autowired
    private AccountSavingCloseService accountSavingCloseService;

    @Autowired
    private CreditCardCloseService creditCardCloseService;

    @Autowired
    private AccountAccessService accountAccessService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountAmountService accountAmountService;

    @Autowired
    private OverdraftLimitService overdraftLimitService;

    @Autowired
    private AccountFreezeService accountFreezeService;

    public AccountOpenProjection openAccount(String name,
                                             String surname,
                                             String initials,
                                             Date date,
                                             String ssn,
                                             String address,
                                             String telephoneNumber,
                                             String email,
                                             String username,
                                             String password) throws InvalidParamValueException {
        return accountOpenService.openAccount(name, surname, initials, date, ssn, address, telephoneNumber, email, username, password);
    }

    public AccountOpenProjection openAdditionalAccount(String authToken) throws NotAuthorizedException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        return accountOpenService.openAdditionalAccount(customerId);
    }

    public void closeAccount(String authToken, String IBAN) throws InvalidParamValueException, NotAuthorizedException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        String normalizedAccountNumber = AccountType.getNormalizedAccount(IBAN);
        if (accountService.checkIfIsMainAccountHolder(normalizedAccountNumber, customerId)) {
            switch (AccountType.getAccountType(IBAN)){
                case CREDIT:
                    creditCardCloseService.closeCreditCard(accountService.getAccountBeanByAccountNumber(normalizedAccountNumber).getAccountId());
                    break;
                case SAVING:
                    accountSavingCloseService.closeAccount(normalizedAccountNumber);
                    break;
                default:
                    accountCloseService.closeAccount(IBAN, customerId);
                    break;
            }
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public PinProjection provideAccess(String authToken, String IBAN, String username) throws InvalidParamValueException, NotAuthorizedException, NoEffectException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        if (accountService.checkIfIsMainAccountHolder(IBAN, customerId)) {
            return accountAccessService.provideAccess(IBAN, username);
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public void revokeAccess(String authToken, String IBAN, String username) throws NotAuthorizedException, InvalidParamValueException, NoEffectException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        if (username == null) {
            accountAccessService.revokeAccess(customerId, IBAN);
        } else {
            if (accountService.checkIfIsMainAccountHolder(IBAN, customerId)) {
                accountAccessService.revokeAccess(customerService.getCustomerBeanByUsername(username).getCustomerId(), IBAN);
            }
        }
    }

    public AccountAmountProjection getBalance(String authToken, String IBAN) throws NotAuthorizedException, InvalidParamValueException {
        if(AuthenticationService.instance.isCustomer(authToken)){
            int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
            if (accountService.checkIfAccountHolder(IBAN, customerId)) {
                return accountAmountService.getBalance(accountService.getAccountBeanByAccountNumber(IBAN).getAccountId());
            }
        }else{
            boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
            if(isAdministrativeEmployee){
                return accountAmountService.getBalance(accountService.getAccountBeanByAccountNumber(IBAN).getAccountId());
            }
        }
        throw new NotAuthorizedException("Not Authorized");
    }

    public List<CustomerUsernameProjection> getBankAccountAccess(String authToken, String IBAN) throws InvalidParamValueException, NotAuthorizedException {
        if(AuthenticationService.instance.isCustomer(authToken)){
            int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
            if (accountService.checkIfIsMainAccountHolder(IBAN, customerId)) {
                return accountAccessService.getBankAccountAccess(accountService.getAccountBeanByAccountNumber(IBAN).getAccountId());
            }
        }else{
            boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
            if(isAdministrativeEmployee){
                return accountAccessService.getBankAccountAccess(accountService.getAccountBeanByAccountNumber(IBAN).getAccountId());
            }
        }
        throw new NotAuthorizedException("Not Authorized");
    }

    public void setOverdraftLimit(String authToken, String iBAN, double overdraftLimit) throws NotAuthorizedException, InvalidParamValueException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        if (accountService.checkIfIsMainAccountHolder(iBAN, customerId)){
            overdraftLimitService.setOverdraft(iBAN, overdraftLimit);
        }else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public AccountOverdraftLimitProjection getOverdraftLimit(String authToken, String iBAN) throws NotAuthorizedException, InvalidParamValueException {
        if(AuthenticationService.instance.isCustomer(authToken)){
            int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
            if (accountService.checkIfIsMainAccountHolder(iBAN, customerId)) {
                return overdraftLimitService.getOverdraft(iBAN);
            }
        }else{
            boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
            if(isAdministrativeEmployee){
                return overdraftLimitService.getOverdraft(iBAN);
            }
        }
        throw new NotAuthorizedException("Not Authorized");
    }

    public void setFreezeUserAccount(String authToken, String iBAN, boolean freeze) throws NotAuthorizedException, InvalidParamValueException, NoEffectException {
        boolean isAdmin = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
        if(isAdmin){
            accountFreezeService.freezeAccount(accountService.getAccountBeanByAccountNumber(iBAN).getAccountId(), freeze);
        }else{
            throw new NotAuthorizedException("Not Authorized");
        }
    }
}
