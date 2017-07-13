package com.bank.controller;

import com.bank.exception.InvalidParamValueException;
import com.bank.service.time.TimeSimulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeController {

    @Autowired
    private TimeSimulateService timeSimulateService;

    public void simulateTime(int nrOfDays) throws InvalidParamValueException {
        timeSimulateService.simulateTime(nrOfDays);
    }
}
