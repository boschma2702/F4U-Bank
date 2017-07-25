package com.bank.controller;

import com.bank.projection.logging.LogEntryProjection;
import com.bank.util.Logging.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoggingController {

    public List<LogEntryProjection> getEventLogs(Date a, Date b){
        return Logger.getLogsBetween(a, b);
    }

}
