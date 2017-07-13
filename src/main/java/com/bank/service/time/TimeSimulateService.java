package com.bank.service.time;

import com.bank.exception.InvalidParamValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSimulateService {

    private static final long DAY_AMOUNT = 86400000;

    @Autowired
    private TimeService timeService;

    public void simulateTime(int nrOfDays) throws InvalidParamValueException {
        if(nrOfDays<=0){
            throw new InvalidParamValueException("Invalid number of days");
        }
        timeService.addTime(nrOfDays*DAY_AMOUNT);
    }
}
