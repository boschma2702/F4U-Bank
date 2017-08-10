package com.bank.controller;

import com.bank.bean.account.AccountBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.exception.NotAuthorizedException;
import com.bank.projection.customer.CustomerUsernameProjection;
import com.bank.projection.pin.PinProjection;
import com.bank.service.AuthenticationService;
import com.bank.service.account.AccountAccessService;
import com.bank.service.account.AccountService;
import com.bank.service.customer.CustomerService;
import com.bank.service.customeraccount.CustomerAccountTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountAccessController {

    @Autowired
    private CustomerAccountTransferService customerAccountTransferService;

    @Autowired
    private AccountAccessService accountAccessService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    public void transferBankAccount(String authToken, String iBAN, String username) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException, NoEffectException {
        boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
        if(isAdministrativeEmployee){
            CustomerBean customerBean = customerService.getCustomerBeanByUsername(username);
            AccountBean accountBean = accountService.getAccountBeanByAccountNumberCheckFrozen(iBAN);
            customerAccountTransferService.transferBankAccount(accountBean.getAccountId(), customerBean.getCustomerId());
        }else{
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public PinProjection provideAccess(String authToken, String IBAN, String username) throws InvalidParamValueException, NotAuthorizedException, NoEffectException, AccountFrozenException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        accountService.checkMinor(IBAN);
        if (accountService.checkIfIsMainAccountHolderCheckFrozen(IBAN, customerId)) {
            return accountAccessService.provideAccess(IBAN, username);
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

    public void revokeAccess(String authToken, String IBAN, String username) throws NotAuthorizedException, InvalidParamValueException, NoEffectException, AccountFrozenException {
        int customerId = (Integer) AuthenticationService.instance.getObject(authToken, AuthenticationService.USER_ID);
        accountService.checkMinor(IBAN);
        if (username == null) {
            accountAccessService.revokeAccess(customerId, accountService.getAccountBeanByAccountNumberCheckFrozen(IBAN).getAccountId());
        } else {
            if (accountService.checkIfIsMainAccountHolderCheckFrozen(IBAN, customerId)) {
                accountAccessService.revokeAccess(customerService.getCustomerBeanByUsername(username).getCustomerId(), accountService.getAccountBeanByAccountNumber(IBAN).getAccountId());
            }
        }
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

}
