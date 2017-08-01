package com.bank.controller;

import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.exception.NotAuthorizedException;
import com.bank.projection.time.DateProjection;
import com.bank.service.AuthenticationService;
import com.bank.service.BackupAndRestoreService;
import com.bank.service.time.TimeInitialService;
import com.bank.service.time.TimeService;
import com.bank.service.time.TimeSimulateService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
public class TimeController {

    @Autowired
    private TimeSimulateService timeSimulateService;

    @Autowired
    private TimeInitialService timeInitialService;

    @Autowired
    private BackupAndRestoreService backupAndRestoreService;

    public void simulateTime(String authToken, int nrOfDays) throws InvalidParamValueException, NoEffectException, NotAuthorizedException {
        boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
        if(isAdministrativeEmployee) {
            timeSimulateService.simulateTime(nrOfDays);
        }
    }

    public void reset(String authToken) throws NoEffectException, NotAuthorizedException {
        boolean isAdministrativeEmployee = (Boolean) AuthenticationService.instance.getObject(authToken, AuthenticationService.HAS_ADMINISTRATIVE_ACCESS);
        if(isAdministrativeEmployee) {
            try {
                Date initialDate = timeInitialService.getInitialDate();
                if (backupAndRestoreService.restore()) {
                    Logger.resetLog(initialDate);
                }
            } catch (IOException | InterruptedException e) {
                throw new NoEffectException("Failed to restore");
            }
        }
    }

    public DateProjection getDate(){
        DateProjection projection = new DateProjection();
        projection.setDate(TimeService.TIMESIMULATOR.getCurrentDate());
        return projection;
    }
}
