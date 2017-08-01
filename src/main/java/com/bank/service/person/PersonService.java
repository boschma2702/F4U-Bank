package com.bank.service.person;

import com.bank.bean.person.PersonBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.person.PersonRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public PersonBean getPersonBeanByUsername(String username) throws InvalidParamValueException {
        Logger.info("Retrieving personBean of username=%s", username);
        PersonBean personBean = personRepository.findPersonBeanByUsername(username);
        if (personBean == null){
            Logger.error("Could not find personBean with username=%s", username);
            throw new InvalidParamValueException("Could not find username");
        }
        return personBean;
    }

}
