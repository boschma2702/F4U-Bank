package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.repository.account.AccountRepository;
import com.bank.repository.customer.CustomerRepository;
import com.bank.service.customer.CustomerAccessService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountFreezeService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerAccessService customerAccessService;

    @Transactional
    public void freezeAccount(int customerId, boolean freeze) throws InvalidParamValueException, NoEffectException {
        Logger.info("About to freeze=%s customerId=%s", freeze, customerId);
        CustomerBean customerBean = customerRepository.getCustomerBeanByCustomerId(customerId);
        if (customerBean.isFrozen() == freeze) {
            Logger.info("Could not freeze=%s customerId=%s, account already (un)frozen", freeze, customerId);
            throw new NoEffectException("Account is already (un)frozen");
        }
        customerBean.setFrozen(freeze);
        customerRepository.save(customerBean);

        //freeze all accounts where customer is main accountHolder
        List<AccountBean> accountBeans = customerAccessService.getMainAccountAccess(customerId);
        for (AccountBean accountBean : accountBeans) {
            accountBean.setFrozen(freeze);
            accountRepository.save(accountBean);
        }
    }

}
