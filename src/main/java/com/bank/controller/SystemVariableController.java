package com.bank.controller;

import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NotAuthorizedException;
import com.bank.service.AuthenticationService;
import com.bank.service.systemvariables.SystemVariableEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class SystemVariableController {

    @Autowired
    private SystemVariableEditorService systemVariableEditorService;

    public void setValue(String authToken, String key, String value, Date date) throws NotAuthorizedException, InvalidParamValueException {
        boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
        if (isAdministrativeEmployee) {
            systemVariableEditorService.setValue(key, value, date);
        }
    }

}
