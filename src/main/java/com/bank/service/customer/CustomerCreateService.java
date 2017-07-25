package com.bank.service.customer;

import com.bank.bean.customer.CustomerBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.customer.CustomerRepository;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CustomerCreateService {
    @Autowired
    private CustomerRepository customerRepository;

    public void createCustomer(CustomerBean customerBean) throws InvalidParamValueException {
        Logger.info("Creating customer with name=%s, surname=%s and username=%s", customerBean.getName(), customerBean.getSurname(), customerBean.getUsername());
        try {
            customerRepository.save(customerBean);
        } catch (DataIntegrityViolationException e) {
            Logger.error("Could not add new customer, username=%s is already present", customerBean.getUsername());
            throw new InvalidParamValueException("Username already present");
        }
    }
}
