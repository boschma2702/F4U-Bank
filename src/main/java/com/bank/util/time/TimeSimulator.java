package com.bank.util.time;

import com.bank.service.time.TimeSimulateService;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TimeSimulator implements Runnable {

    private long timeChange;
    private long toAdd;
    private Thread thread;
    private PriorityQueue<DayPassedListener> dayPassedListeners = new PriorityQueue<>();

    public TimeSimulator(long timeChange) {
        this.timeChange = timeChange;
        thread = new Thread(this);
        thread.start();
    }

    public Date getCurrentDate() {
        return new Date(new Date().getTime() + timeChange);
    }

    public long getTimeChange() {
        return timeChange;
    }

    public void addTimeChange(long amount) {
        toAdd = amount;
        thread.interrupt();
    }

    public void registerDayPassedListener(DayPassedListener dayPassedListener) {
        dayPassedListeners.add(dayPassedListener);
    }

    private void notifyDayPassedListeners(Date start, Date end) {
        for (DayPassedListener listener : dayPassedListeners) {
            listener.onDayPassed(start, end);
        }
    }

    public void reset() {
        timeChange = 0;
        toAdd = 0;
        thread.interrupt();
    }


    @Override
    public void run() {
        for (; ; ) {
            Calendar goalDate = Calendar.getInstance();
            Date currentDate = getCurrentDate();
            goalDate.setTime(currentDate);
            //Set calender to 00:00:000 of new day
            goalDate.set(Calendar.HOUR_OF_DAY, 0);
            goalDate.set(Calendar.MINUTE, 0);
            goalDate.set(Calendar.SECOND, 0);
            goalDate.set(Calendar.MILLISECOND, 0);
            goalDate.add(Calendar.DAY_OF_MONTH, 1);

            long sleepTime = goalDate.getTime().getTime() - currentDate.getTime();

            try {
                Thread.sleep(sleepTime);
                //day is over
                notifyDayPassedListeners(getStartOfDay(goalDate), getEndOfDay(goalDate));
            } catch (InterruptedException e) {
                //skip if it was a reset
                if (timeChange == 0 && toAdd == 0) {
                    continue;
                }

                while(toAdd>0) {
                    //Check if possible to simulate another day
                    currentDate = getCurrentDate();
                    long timeStillShort = goalDate.getTimeInMillis() - currentDate.getTime();
                    if (toAdd > timeStillShort) {
                        //simulate to 00:00.000 next day (extract from toAdd and add to timeChange
                        toAdd -= timeStillShort;
                        timeChange += timeStillShort;
                        //notify day passed listeners
                        notifyDayPassedListeners(getStartOfDay(goalDate), getEndOfDay(goalDate));
                        //set new goalDate
                        goalDate.add(Calendar.DAY_OF_MONTH, 1);
                    } else {
                        timeChange += toAdd;
                        toAdd = 0;
                    }
                }
            }

        }
    }

    private Date getStartOfDay(Calendar calendar) {
        Calendar start = Calendar.getInstance();
        start.setTime(calendar.getTime());
        start.add(Calendar.DAY_OF_MONTH, -1);
        return start.getTime();
    }

    private Date getEndOfDay(Calendar calendar) {
        Calendar end = Calendar.getInstance();
        end.setTime(calendar.getTime());
        end.add(Calendar.MILLISECOND, -1);
        return end.getTime();
    }


}
