package com.bank.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeSimulator implements Runnable {

    private long timeChange;
    private Thread thread;
    private List<DayPassedListener> dayPassedListeners = new ArrayList<>();

    public TimeSimulator(long timeChange) {
        this.timeChange = timeChange;
        thread = new Thread(this);
        thread.start();
    }

    public Date getCurrentDate(){
        return new Date(new Date().getTime()+timeChange);
    }

    public long getTimeChange() {
        return timeChange;
    }

    public void addTimeChange(long amount){
        timeChange = timeChange + amount;
        thread.interrupt();
    }

    public void registerDayPassedListener(DayPassedListener dayPassedListener){
        dayPassedListeners.add(dayPassedListener);
    }

    private void notifyDayPassedListeners(Date start, Date end){
        for(DayPassedListener listener : dayPassedListeners){
            listener.onDayPassed(start, end);
        }
    }

    public void reset(){
        timeChange = 0;
    }


    @Override
    public void run() {
        for(;;){
            Calendar calendar = Calendar.getInstance();
            Date currentDate = getCurrentDate();
            calendar.setTime(currentDate);
            //Set calender to 00:00:000 of new day
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            long sleepTime = calendar.getTime().getTime()-currentDate.getTime();

            try {
                Thread.sleep(sleepTime);
                //day is over
                notifyDayPassedListeners(getStartOfDay(calendar), getEndOfDay(calendar));
            } catch (InterruptedException e) {
                // time is changed
                long timeChanged = getCurrentDate().getTime()-calendar.getTime().getTime();
                int daysChanged = (int) TimeUnit.DAYS.convert(timeChanged, TimeUnit.MILLISECONDS);

                for(int i=0; i<=daysChanged; i++){
                    notifyDayPassedListeners(getStartOfDay(calendar), getEndOfDay(calendar));
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }

        }
    }

    private Date getStartOfDay(Calendar calendar){
        Calendar start = Calendar.getInstance();
        start.setTime(calendar.getTime());
        start.add(Calendar.DAY_OF_MONTH, -1);
        return start.getTime();
    }

    private Date getEndOfDay(Calendar calendar){
        Calendar end = Calendar.getInstance();
        end.setTime(calendar.getTime());
        end.add(Calendar.MILLISECOND, -1);
        return end.getTime();
    }


}
