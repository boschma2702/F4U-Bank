package com.bank.service.customer;

import com.bank.bean.customer.CustomerBean;
import com.bank.repository.customer.CustomerRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public CustomerBean getCustomerBeanById(int customerId) {
        Logger.info("Retrieving CustomerBean from customerId=%s", customerId);
        return customerRepository.getCustomerBeanByCustomerId(customerId);
    }

    public CustomerBean getCustomerBeanByUsername(String username) {
        Logger.info("Retrieving CustomerBean from username=%s", username);
        return customerRepository.getCustomerBeanByUsername(username);
    }
}
