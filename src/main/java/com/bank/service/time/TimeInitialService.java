package com.bank.service.time;

import com.bank.bean.systeminfo.SystemInfo;
import com.bank.exception.NoEffectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TimeInitialService {

    @Autowired
    private TimeService timeService;

    public Date getInitialDate() throws NoEffectException {
        SystemInfo systemInfo = timeService.getSystemInfo();
        return systemInfo.getInitialDate();
    }

}

