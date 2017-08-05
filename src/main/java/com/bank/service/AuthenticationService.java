package com.bank.service;

import com.bank.exception.AuthenticationException;
import com.bank.exception.NotAuthorizedException;
import com.bank.util.logging.Logger;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class AuthenticationService implements Runnable {
    public static final String USER_ID = "userId";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String HAS_ADMINISTRATIVE_ACCESS = "administrativeEmployee";

    // 30 minutes
    private static final long COOKIE_VALID_TIME = 30*60000;
    // halve minute
    private static final long COOKIE_CHECK_INTERVAL = 30000;

    private static final Lock COOKIE_LOCK = new ReentrantLock();

    public static AuthenticationService instance = new AuthenticationService();

    private static HashMap<String, HashMap<String, Object>> map = new HashMap<String, HashMap<String, Object>>();
    private static HashMap<String, Long> cookieTimes = new HashMap();

    private SecureRandom random = new SecureRandom();

    public AuthenticationService() {
        new Thread(this).start();
    }

    public String customerLogin(int userId) {
        Logger.info("Login of userId=%s", userId);
        String token = generateToken();
        map.put(token, new HashMap<String, Object>());
        map.get(token).put(USER_ID, userId);
        cookieTimes.put(token, COOKIE_VALID_TIME);
        return token;
    }

    public String employeeLogin(int employeeId, boolean administrativeEmployee){
        Logger.info("Login of employeeId=%s", employeeId);
        String token = generateToken();
        HashMap<String, Object> info = new HashMap<>();
        info.put(EMPLOYEE_ID, employeeId);
        info.put(HAS_ADMINISTRATIVE_ACCESS, administrativeEmployee);
        map.put(token, info);
        cookieTimes.put(token, COOKIE_VALID_TIME);
        return token;
    }

    private String generateToken() {
        return new BigInteger(130, random).toString(32);
    }


    public final boolean isAuthenticated(String token) {
        boolean isAuthenticated = map.containsKey(token);
        if(isAuthenticated){
            cookieTimes.put(token, COOKIE_VALID_TIME);
        }
        return isAuthenticated;
    }

    public final void setObject(String token, String key, Object value) throws AuthenticationException {
        if (isAuthenticated(token)) {
            map.get(token).put(key, value);
        } else {
            throw new AuthenticationException("Not authorized: setObject");
        }
    }

    public final Object getObject(String token, String key) throws NotAuthorizedException {
        if (isAuthenticated(token)) {
            if (map.get(token).get(key) != null) {
                return map.get(token).get(key);
            } else {
                throw new NotAuthorizedException("Not authorized: getObject");
            }
        } else {
            throw new NotAuthorizedException("Not authorized: getObject");
        }
    }

    public boolean isCustomer(String token) throws NotAuthorizedException {
        if(isAuthenticated(token)){
            if(map.get(token).containsKey(USER_ID)){
                return true;
            }else if(map.get(token).containsKey(EMPLOYEE_ID)){
                return false;
            }
        }
        throw new NotAuthorizedException("not Authorized");
    }

    public static void removeCookies(){
        try{
            COOKIE_LOCK.lock();
            cookieTimes = new HashMap<>();
            map = new HashMap<>();
        }finally {
            COOKIE_LOCK.unlock();
        }
    }

    @Override
    public void run() {
        for(;;){
            try {
                Thread.sleep(COOKIE_CHECK_INTERVAL);
                try {
                    COOKIE_LOCK.lock();
                    Iterator<String> iterator = cookieTimes.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        long remainingTime = cookieTimes.get(key) - COOKIE_CHECK_INTERVAL;
                        if (remainingTime < 0) {
                            cookieTimes.remove(key);
                            map.remove(key);
                            Logger.info("Removing key=%s", key);
                        } else {
                            cookieTimes.put(key, remainingTime);
                        }
                    }
                }finally {
                    COOKIE_LOCK.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Logger.error("Cookie checker got interrupted while sleeping.");
            }
        }
    }
}
