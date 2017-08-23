package com.bank.service.customer;

import com.bank.repository.customer.CustomerRepository;
import com.bank.repository.person.PersonRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CustomerCloseService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PersonRepository personRepository;

    public void closeCustomer(int customerId) {
        Logger.info("Closing customerId=%s", customerId);
        customerRepository.closeCustomer(customerId);
        personRepository.deletePersonByCustomerId(customerId);
    }
}
