package com.bank.service.customer;

import com.bank.bean.person.PersonBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.customer.CustomerRepository;
import com.bank.repository.person.PersonRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CustomerCreateService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PersonRepository personRepository;

    public void createCustomer(PersonBean personBean) throws InvalidParamValueException {
        Logger.info("Creating customer with name=%s, surname=%s and username=%s", personBean.getCustomerBean().getName(), personBean.getCustomerBean().getSurname(), personBean.getUsername());
        try {
            personRepository.save(personBean);
        } catch (DataIntegrityViolationException e) {
            Logger.error("Could not add new customer, username=%s is already present", personBean.getUsername());
            throw new InvalidParamValueException("Username already present");
        }
    }
}
