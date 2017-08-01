package com.bank.service.employee;

import com.bank.exception.InvalidParamValueException;
import com.bank.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDefaultService {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    public EmployeeDefaultService(EmployeeAddService employeeAddService){
        try {
            Logger.info("Adding default administrate user");
            employeeAddService.addEmployee(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        } catch (InvalidParamValueException e) {
            //if already present do nothing
        }
    }

}
