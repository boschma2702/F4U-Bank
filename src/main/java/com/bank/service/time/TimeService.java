package com.bank.service.time;

import com.bank.bean.systeminfo.SystemInfo;
import com.bank.repository.systeminfo.SystemInfoRepository;
import com.bank.util.TimeSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
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
            SystemInfo systemInfo = new SystemInfo();
            systemInfo.setTimeDiff(timeDiff);
            systemInfo.setInitialDate(new Date());
            systemInfoRepository.save(systemInfo);
        }
//        timeDiff = timeDiff + (long)3.1536E10;
        TIMESIMULATOR = new TimeSimulator(timeDiff);
        this.systemInfoRepository = systemInfoRepository;
    }

    @PreDestroy
    public void test(){
        Iterator<SystemInfo> iterator = systemInfoRepository.findAll().iterator();
        if(iterator.hasNext()){
            SystemInfo systemInfo = iterator.next();
            systemInfo.setTimeDiff(TIMESIMULATOR.getTimeChange());
            systemInfoRepository.save(systemInfo);
        }else{
            // This code should never be executed. This is only in place when data loss took place
            SystemInfo systemInfo = new SystemInfo();
            systemInfo.setTimeDiff(TIMESIMULATOR.getTimeChange());
            systemInfo.setInitialDate(new Date(new Date().getTime()-TIMESIMULATOR.getTimeChange()));
            systemInfoRepository.save(systemInfo);
        }
    }

}
