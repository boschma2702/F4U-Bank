package com.bank.service.customer;

import com.bank.repository.customer.CustomerInformationRepository;
import com.bank.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerGetService {
    @Autowired
    private CustomerInformationRepository customerInformationRepository;

    public int getCustomerId(String username) {
        Logger.info("Retrieving customerId of username=%s", username);
        return customerInformationRepository.findByUsername(username).getCustomerId();
    }
}
