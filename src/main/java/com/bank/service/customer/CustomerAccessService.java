package com.bank.service.customer;

import com.bank.bean.account.AccountBean;
import com.bank.projection.customer.CustomerAccountAccessProjection;
import com.bank.repository.customer.CustomerRepository;
import com.bank.util.logging.Logger;
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

    public List<AccountBean> getMainAccountAcces(int customerId) {
        Logger.info("Retrieving main accounts of customerId=%s", customerId);
        return customerRepository.getCustomerBeanMainAccess(customerId);
    }

}
