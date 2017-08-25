package com.bank.service.customeraccount;

import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.repository.customeraccount.CustomerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerAccountTransferService {

    @Autowired
    private CustomerAccountService customerAccountService;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Transactional
    public void transferBankAccount(int accountId, int customerId) throws InvalidParamValueException, NoEffectException {
        CustomerAccount mainCustomerAccount = customerAccountService.getMainCustomerAccountOfAccountId(accountId);
        CustomerAccount customerAccount;
        try {
            customerAccount = customerAccountService.getCustomerAccountByAccountIdAndCustomerId(accountId, customerId);
            if (customerAccount.isMain()) {
                throw new NoEffectException("Customer already main account holder");
            }
        } catch (InvalidParamValueException e) {
            customerAccount = new CustomerAccount();
            customerAccount.setAccountId(accountId);
            customerAccount.setCustomerId(customerId);
        }
        customerAccount.setMain(true);
        customerAccountRepository.delete(mainCustomerAccount);
        customerAccountRepository.save(customerAccount);
    }

}
