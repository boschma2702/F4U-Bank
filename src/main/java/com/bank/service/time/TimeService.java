package com.bank.service.time;

import com.bank.bean.systeminfo.SystemInfo;
import com.bank.exception.NoEffectException;
import com.bank.repository.systeminfo.SystemInfoRepository;
import com.bank.util.TimeSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;

@Service
public class TimeService {

    public static TimeSimulator TIMESIMULATOR;

    private final SystemInfoRepository systemInfoRepository;

    @Autowired
    public TimeService(SystemInfoRepository systemInfoRepository) {
        Iterator<SystemInfo> iterator = systemInfoRepository.findAll().iterator();
        long timeDiff;
        if(iterator.hasNext()){
            timeDiff = iterator.next().getTimeDiff();
        }else{
            timeDiff = 0;

        }
        TIMESIMULATOR = new TimeSimulator(timeDiff);
        this.systemInfoRepository = systemInfoRepository;
    }

    public void addTime(long amount){
        TIMESIMULATOR.addTimeChange(amount);

        Iterator<SystemInfo> iterator = systemInfoRepository.findAll().iterator();
        SystemInfo systemInfo;
        if(iterator.hasNext()){
            systemInfo = iterator.next();
        }else{
            systemInfo = new SystemInfo();
            systemInfo.setInitialDate(new Date());
        }
        systemInfo.setTimeDiff(TimeService.TIMESIMULATOR.getTimeChange());
        systemInfoRepository.save(systemInfo);
    }

    public SystemInfo getSystemInfo() throws NoEffectException {
        Iterator<SystemInfo> iterator = systemInfoRepository.findAll().iterator();
        if(iterator.hasNext()){
            return iterator.next();
        }
        throw new NoEffectException("Nothing to go back to");
    }

}
