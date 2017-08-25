package com.bank.controller;

import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NotAuthorizedException;
import com.bank.service.AuthenticationService;
import com.bank.service.systemvariables.SystemVariableAddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;

@Service
public class SystemVariableController {

    @Autowired
    private SystemVariableAddService systemVariableAddService;

    public void setValue(String authToken, String key, BigDecimal value, Date date) throws NotAuthorizedException, InvalidParamValueException {
        boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
        if (isAdministrativeEmployee) {
            systemVariableAddService.setValue(key, value, date);
        }
    }

}
