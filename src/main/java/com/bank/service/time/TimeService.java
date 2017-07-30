package com.bank.service.time;

import com.bank.bean.systeminfo.SystemInfo;
import com.bank.exception.NoEffectException;
import com.bank.repository.systeminfo.SystemInfoRepository;
import com.bank.service.BackupAndRestoreService;
import com.bank.util.logging.Logger;
import com.bank.util.time.TimeSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.Iterator;

@Service
public class TimeService {

    /**
     * This object maintains the time used by the server. When one wants to know the time of the system, they should use
     * TimeService.TIMERSIMULATOR.getCurrentDay();
     */
    public static TimeSimulator TIMESIMULATOR;

    private final SystemInfoRepository systemInfoRepository;

    @Autowired
    private BackupAndRestoreService backupAndRestoreService;



    @Autowired
    public TimeService(SystemInfoRepository systemInfoRepository) {
        Iterator<SystemInfo> iterator = systemInfoRepository.findAll().iterator();
        long timeDiff;
        if (iterator.hasNext()) {
            timeDiff = iterator.next().getTimeDiff();
        } else {
            timeDiff = 0;

        }
        TIMESIMULATOR = new TimeSimulator(timeDiff);
        this.systemInfoRepository = systemInfoRepository;
        Logger.info("System time initialized, currentTime=%s", TIMESIMULATOR.getCurrentDate());
    }

    /**
     * Adds the given amount to the time used by the system. This also immediately saves the change to the database.
     *
     * @param amount amount of time that needs to pass. Is in milliseconds.
     */
    public void addTime(long amount) throws NoEffectException {
        Date initialDate = new Date();
        Logger.info("Adding time, amount=%s", amount);
        //Check if first time a timejump took place
        if(isFirstTimeJump()){
            try {
                Logger.info("Creating backup of database");
                backupAndRestoreService.backup();
                SystemInfo systemInfo = new SystemInfo();
                systemInfo.setInitialDate(initialDate);
                systemInfo.setTimeDiff(amount);
                systemInfoRepository.save(systemInfo);
            } catch (InterruptedException | IOException e) {
                Logger.error("Could not create backup of database");
                throw new NoEffectException("could not create backup");
            }
        }else{
            SystemInfo systemInfo = getSystemInfo();
            systemInfo.setTimeDiff(systemInfo.getTimeDiff()+amount);
            systemInfoRepository.save(systemInfo);
        }
        TIMESIMULATOR.addTimeChange(amount);
    }

    /**
     * Returns the SystemInfo Object. No changes should be made directly to this class.
     *
     * @return the systemInfoObject
     * @throws NoEffectException if the no row was present in the database. This means no time change took place.
     */
    public SystemInfo getSystemInfo() throws NoEffectException {
        Iterator<SystemInfo> iterator = systemInfoRepository.findAll().iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoEffectException("Nothing to go back to");
    }

    /**
     * Checks whether there is a SystemInfo row present
     * @return true if it is present, false if not
     */
    public boolean isFirstTimeJump() {
        Iterator<SystemInfo> iterator = systemInfoRepository.findAll().iterator();
        if (iterator.hasNext()) {
            return false;
        }
        return true;
    }




}
