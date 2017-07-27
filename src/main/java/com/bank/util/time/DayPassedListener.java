package com.bank.util.time;

import com.bank.service.time.TimeService;

import java.util.Date;

public abstract class DayPassedListener implements Comparable<DayPassedListener> {

    public DayPassedListener() {
        TimeService.TIMESIMULATOR.registerDayPassedListener(this);
    }

    public abstract void onDayPassed(Date start, Date end);

    public abstract int getOrder();

    @Override
    public int compareTo(DayPassedListener o) {
        if(getOrder()==o.getOrder()){
            return 0;
        }
        if (getOrder()<o.getOrder()){
            return -1;
        }
        return 1;
    }
}
