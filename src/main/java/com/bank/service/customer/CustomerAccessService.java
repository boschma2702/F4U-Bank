package com.bank.service.customer;

import com.bank.projection.customer.CustomerAccountAccessProjection;
import com.bank.repository.customer.CustomerRepository;
import com.bank.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerAccessService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerAccountAccessProjection> getUserAccess(int customerId) {
        Logger.info("Retrieve CustomerAccountAccessProjection associated to customerId=%s", customerId);
        return customerRepository.getCustomerAccess(customerId);
    }
}
