package com.bank.projection.logging;

public class LogEntryProjection {

    private String timeStamp;
    private String eventLog;

    public LogEntryProjection(String timeStamp, String eventLog) {
        this.timeStamp = timeStamp;
        this.eventLog = eventLog;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getEventLog() {
        return eventLog;
    }

    public void setEventLog(String eventLog) {
        this.eventLog = eventLog;
    }
}
