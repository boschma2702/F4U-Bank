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


    @Override
    public void run() {
        for(;;){
            Calendar calendar = Calendar.getInstance();
            Date currentDate = getCurrentDate();
            calendar.setTime(currentDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 0);

            long sleepTime = calendar.getTime().getTime()-currentDate.getTime();
            if(sleepTime<=0){
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                sleepTime = calendar.getTime().getTime()-currentDate.getTime();
            }

            try {
                Thread.sleep(sleepTime);
                //day is over

                notifyDayPassedListeners(getStartOfDay(currentDate), getEndOfDay(currentDate));
            } catch (InterruptedException e) {
                // time is changed
                long timeChanged = getCurrentDate().getTime()-currentDate.getTime();
                int daysChanged = (int) TimeUnit.DAYS.convert(timeChanged, TimeUnit.MILLISECONDS);

                Date date = new Date(currentDate.getTime());
                for(int i=0; i<daysChanged; i++){
                    notifyDayPassedListeners(getStartOfDay(date), getEndOfDay(date));
                    calendar.setTime(date);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }

        }
    }

    private Date getStartOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    private Date getEndOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }


}
