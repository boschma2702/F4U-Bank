package com.bank.service.time;

import com.bank.exception.InvalidParamValueException;
import org.springframework.stereotype.Service;

@Service
public class TimeSimulateService {

    private static final long DAY_AMOUNT = 86400000;

    public void simulateTime(int nrOfDays) throws InvalidParamValueException {
        if(nrOfDays<=0){
            throw new InvalidParamValueException("Invalid number of days");
        }
        TimeService.TIMESIMULATOR.addTimeChange(nrOfDays*DAY_AMOUNT);
    }
}
