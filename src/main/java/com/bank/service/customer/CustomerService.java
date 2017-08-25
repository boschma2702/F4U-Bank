package com.bank.service.customer;

import com.bank.bean.customer.CustomerBean;
import com.bank.bean.person.PersonBean;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.customer.CustomerRepository;
import com.bank.service.person.PersonService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PersonService personService;

    public CustomerBean getCustomerBeanById(int customerId) {
        Logger.info("Retrieving CustomerBean from customerId=%s", customerId);
        return customerRepository.getCustomerBeanByCustomerId(customerId);
    }

    public CustomerBean getCustomerBeanByUsername(String username) throws InvalidParamValueException {
        Logger.info("Retrieving CustomerBean from username=%s", username);
        PersonBean personBean = personService.getPersonBeanByUsername(username);
        if (personBean.getCustomerBean() == null) {
            Logger.error("Person with username=%s is not a customer", username);
            throw new InvalidParamValueException("Person is not a customer");
        }
        return personBean.getCustomerBean();
    }

    public void checkIfCustomerIsFrozen(int customerId) throws AccountFrozenException {
        Logger.info("Checking if customreId=%s is frozen", customerId);
        if (customerRepository.isCustomerFrozen(customerId)) {
            throw new AccountFrozenException("Customer account is frozen");
        }
    }
}
