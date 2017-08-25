package com.bank.controller;

import com.bank.exception.NotAuthorizedException;
import com.bank.projection.logging.LogEntryProjection;
import com.bank.service.AuthenticationService;
import com.bank.util.logging.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoggingController {

    public List<LogEntryProjection> getEventLogs(String authToken, Date a, Date b) throws NotAuthorizedException {
        boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
        if (isAdministrativeEmployee) {
            return Logger.getLogsBetween(a, b);
        } else {
            throw new NotAuthorizedException("Not Authorized");
        }
    }

}
