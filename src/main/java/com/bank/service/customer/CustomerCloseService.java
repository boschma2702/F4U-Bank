package com.bank.service.customer;

import com.bank.repository.customer.CustomerRepository;
import com.bank.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CustomerCloseService {
    @Autowired
    private CustomerRepository customerRepository;

    public void closeCustomer(int customerId) {
        Logger.info("Closing customerId=%s", customerId);
        customerRepository.closeCustomer(customerId);
    }
}
