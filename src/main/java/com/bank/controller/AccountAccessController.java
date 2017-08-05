package com.bank.controller;

import com.bank.bean.account.AccountBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.exception.NotAuthorizedException;
import com.bank.service.AuthenticationService;
import com.bank.service.account.AccountService;
import com.bank.service.customer.CustomerService;
import com.bank.service.customeraccount.CustomerAccountTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountAccessController {

    @Autowired
    private CustomerAccountTransferService customerAccountTransferService;

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


}
