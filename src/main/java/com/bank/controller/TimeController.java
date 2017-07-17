package com.bank.controller;

import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.projection.time.DateProjection;
import com.bank.service.BackupAndRestoreService;
import com.bank.service.time.TimeService;
import com.bank.service.time.TimeSimulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TimeController {

    @Autowired
    private TimeSimulateService timeSimulateService;

//    @Autowired
//    private TimeResetService timeResetService;

    @Autowired
    private BackupAndRestoreService backupAndRestoreService;

    public void simulateTime(int nrOfDays) throws InvalidParamValueException, NoEffectException {
        timeSimulateService.simulateTime(nrOfDays);
    }

    public void reset() throws NoEffectException {
        try {
            backupAndRestoreService.restore();
        } catch (IOException | InterruptedException e) {
            throw new NoEffectException("Failed to restore");
        }
    }

    public DateProjection getDate(){
        DateProjection projection = new DateProjection();
        projection.setDate(TimeService.TIMESIMULATOR.getCurrentDate());
        return projection;
    }
}
