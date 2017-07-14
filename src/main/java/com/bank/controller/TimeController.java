package com.bank.controller;

import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.projection.time.DateProjection;
import com.bank.service.time.TimeResetService;
import com.bank.service.time.TimeService;
import com.bank.service.time.TimeSimulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeController {

    @Autowired
    private TimeSimulateService timeSimulateService;

    @Autowired
    private TimeResetService timeResetService;

    public void simulateTime(int nrOfDays) throws InvalidParamValueException {
        timeSimulateService.simulateTime(nrOfDays);
    }

    public void reset() throws NoEffectException {
        timeResetService.reset();
    }

    public DateProjection getDate(){
        DateProjection projection = new DateProjection();
        projection.setDate(TimeService.TIMESIMULATOR.getCurrentDate());
        return projection;
    }
}