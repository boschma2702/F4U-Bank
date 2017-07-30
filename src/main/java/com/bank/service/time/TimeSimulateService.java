package com.bank.service.time;

import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class TimeSimulateService {

    public static final long DAY_AMOUNT = 86400000;

    @Autowired
    private TimeService timeService;

    public void simulateTime(int nrOfDays) throws InvalidParamValueException, NoEffectException {
        if (nrOfDays <= 0) {
            Logger.error("Invalid number of days, nrOfDays=%s", nrOfDays);
            throw new InvalidParamValueException("Invalid number of days");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeService.TIMESIMULATOR.getCurrentDate());
        calendar.add(Calendar.DAY_OF_MONTH, nrOfDays);
        long toAdd = calendar.getTimeInMillis() - TimeService.TIMESIMULATOR.getCurrentDate().getTime();
        timeService.addTime(toAdd);
        Logger.info("Simulated time, nrOfDays=%s", nrOfDays);
    }
}
