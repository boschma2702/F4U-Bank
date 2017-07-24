package com.bank.service.time;

import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSimulateService {

    private static final long DAY_AMOUNT = 86400000;

    @Autowired
    private TimeService timeService;

    public void simulateTime(int nrOfDays) throws InvalidParamValueException, NoEffectException {
        Logger.info("Simulating time, nrOfDays=%s", nrOfDays);
        if(nrOfDays<=0){
            Logger.error("Invalid number of days, nrOfDays=%s", nrOfDays);
            throw new InvalidParamValueException("Invalid number of days");
        }
        timeService.addTime(nrOfDays*DAY_AMOUNT);
    }
}
