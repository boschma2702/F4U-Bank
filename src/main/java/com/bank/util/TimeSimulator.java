package com.bank.util;

import java.util.Date;

public class TimeSimulator {

    private long timeChange;

    public TimeSimulator(long timeChange) {
        this.timeChange = timeChange;
    }

    public Date getCurrentDate(){
        return new Date(new Date().getTime()+timeChange);
    }

    public long getTimeChange() {
        return timeChange;
    }

    public void addTimeChange(long amount){
        timeChange = timeChange + amount;
    }
}
