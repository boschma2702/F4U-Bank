package com.bank.util;

import com.bank.service.time.TimeService;

import java.sql.Date;
import java.util.Calendar;

public class AgeChecker {


    /**
     * Checks if the given birthDate is still below Constatns.AGE_MINOR_MAX.
     *
     * @param birthDate
     * @return true if it is, false if not
     */
    public static boolean isMinor(Date birthDate) {
        return isUnderAge(Constants.AGE_MINOR_MAX, birthDate);
    }

    /**
     * Function that checks if someone with the given birthDate is under the given age.
     *
     * @param age       to check
     * @param birthDate to check
     * @return true if birthDate is NOT sufficient to reach the given age, false if it is.
     */
    public static boolean isUnderAge(int age, Date birthDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeService.TIMESIMULATOR.getCurrentDate());
        calendar.add(Calendar.YEAR, -age);
        return calendar.getTimeInMillis() < birthDate.getTime();
    }

}
