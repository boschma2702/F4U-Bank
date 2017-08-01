package com.bank.service.employee;

import com.bank.bean.employee.EmployeeBean;
import com.bank.bean.person.PersonBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.person.PersonRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAddService {

    @Autowired
    private PersonRepository personRepository;

    public void addEmployee(String username, String password) throws InvalidParamValueException {
        Logger.info("Adding employee with username=%s", username);
        PersonBean personBean = new PersonBean();
        personBean.setUsername(username);
        personBean.setPassword(password);

        EmployeeBean employeeBean = new EmployeeBean();
        personBean.setEmployeeBean(employeeBean);

        try {
            personRepository.save(personBean);
        } catch (DataIntegrityViolationException e) {
            Logger.error("Could not add new employee, username=%s is already present", personBean.getUsername());
            throw new InvalidParamValueException("Username already present");
        }
    }

}
