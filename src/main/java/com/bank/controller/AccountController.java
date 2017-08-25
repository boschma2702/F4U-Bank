package com.bank.controller;

import com.bank.bean.account.AccountBean;
import com.bank.exception.*;
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
import com.bank.util.Constants;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private AccountAmountService accountAmountService;

    @Autowired
    private OverdraftLimitService overdraftLimitService;

    @Autowired
    private AccountFreezeService accountFreezeService;

    @Autowired
    private AccountTransferLimitService accountTransferLimitService;

    @Autowired
    private CustomerService customerService;

    public AccountOpenProjection openAccount(String name,
                                             String surname,
                                             String initials,
                                             Date date,
                                             String ssn,
                                             String address,
                                             String telephoneNumber,
                                             String email,
                                             String username,
                                             String password) throws InvalidParamValueException, NotAuthorizedException {
        try {
            return openAccount(name, surname, initials, date, ssn, address, telephoneNumber, email, username, password, Constants.ACCOUNT_TYPE_REGULAR, new String[]{});
        } catch (AccountFrozenException e) {
            //new accounts without guardians can not throw accountFrozenException
        }
        throw new IllegalStateException("FrozenAccountException during opening new account");
    }

    public AccountOpenProjection openAdditionalAccount(String authToken) throws NotAuthorizedException, AccountFrozenException, NotAllowedException {
        if (AuthenticationService.instance.isCustomer(authToken)) {
            int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
            if ((Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.IS_MINOR)){
                throw new NotAllowedException("Not Authorized");
            }
            return accountOpenService.openAdditionalAccount(customerId);
        }else{
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public void closeAccount(String authToken, String IBAN) throws InvalidParamValueException, NotAuthorizedException, AccountFrozenException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        String normalizedAccountNumber = AccountType.getNormalizedAccount(IBAN);
        if (accountService.checkIfIsMainAccountHolderCheckFrozen(normalizedAccountNumber, customerId)) {
            switch (AccountType.getAccountType(IBAN)) {
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


    public AccountAmountProjection getBalance(String authToken, String IBAN) throws NotAuthorizedException, InvalidParamValueException {
        if (AuthenticationService.instance.isCustomer(authToken)) {
            int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
            if (accountService.checkIfAccountHolder(IBAN, customerId)) {
                return accountAmountService.getBalance(accountService.getAccountBeanByAccountNumber(IBAN).getAccountId());
            }
        } else {
            boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
            if (isAdministrativeEmployee) {
                return accountAmountService.getBalance(accountService.getAccountBeanByAccountNumber(IBAN).getAccountId());
            }
        }
        throw new NotAuthorizedException("Not Authorized");
    }


    public void setOverdraftLimit(String authToken, String iBAN, double overdraftLimit) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException, NotAllowedException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        if (accountService.checkIfIsMainAccountHolderCheckFrozen(iBAN, customerId)) {
            accountService.checkMinor(iBAN);
            overdraftLimitService.setOverdraft(iBAN, overdraftLimit);
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public AccountOverdraftLimitProjection getOverdraftLimit(String authToken, String iBAN) throws NotAuthorizedException, InvalidParamValueException {
        if (AuthenticationService.instance.isCustomer(authToken)) {
            int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
            if (accountService.checkIfIsMainAccountHolder(iBAN, customerId)) {
                return overdraftLimitService.getOverdraft(iBAN);
            }
        } else {
            boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
            if (isAdministrativeEmployee) {
                return overdraftLimitService.getOverdraft(iBAN);
            }
        }
        throw new NotAuthorizedException("Not Authorized");
    }

    public void setFreezeUserAccount(String authToken, String username, boolean freeze) throws NotAuthorizedException, InvalidParamValueException, NoEffectException {
        boolean isAdmin = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
        if (isAdmin) {
            accountFreezeService.freezeAccount(customerService.getCustomerBeanByUsername(username).getCustomerId(), freeze);
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public void setTransferLimit(String authToken, String iBAN, BigDecimal transferLimit) throws InvalidParamValueException, AccountFrozenException, NotAuthorizedException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        if (accountService.checkIfIsMainAccountHolderCheckFrozen(iBAN, customerId)) {
            accountTransferLimitService.setTransferLimit(accountService.getAccountBeanByAccountNumberCheckFrozen(iBAN).getAccountId(), transferLimit);
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public AccountOpenProjection openAccount(String name,
                                             String surname,
                                             String initials,
                                             Date date,
                                             String ssn,
                                             String address,
                                             String telephoneNumber,
                                             String email,
                                             String username,
                                             String password,
                                             String type,
                                             String[] guardians) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException {

        return accountOpenService.openAccount(name, surname, initials, date, ssn, address, telephoneNumber, email, username, password, type, guardians);
    }
}
