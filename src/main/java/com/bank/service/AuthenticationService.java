package com.bank.service;

import com.bank.exception.AuthenticationException;
import com.bank.exception.NotAuthorizedException;
import com.bank.util.logging.Logger;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

public final class AuthenticationService {
    public static final String USER_ID = "userId";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String ADMINISTRATIVE_EMPLOYEE = "administrativeEmployee";

    public static AuthenticationService instance = new AuthenticationService();

    private final HashMap<String, HashMap<String, Object>> map = new HashMap<String, HashMap<String, Object>>();

    private SecureRandom random = new SecureRandom();

    public AuthenticationService() {

    }

    public String customerLogin(int userId) {
        Logger.info("Login of userId=%s", userId);
        String token = generateToken();
        map.put(token, new HashMap<String, Object>());
        map.get(token).put(USER_ID, userId);
        return token;
    }

    public String employeeLogin(int employeeId, boolean administrativeEmployee){
        Logger.info("Login of employeeId=%s", employeeId);
        String token = generateToken();
        HashMap<String, Object> info = new HashMap<>();
        info.put(EMPLOYEE_ID, employeeId);
        info.put(ADMINISTRATIVE_EMPLOYEE, administrativeEmployee);
        map.put(token, info);
        return token;
    }

    private String generateToken() {
        return new BigInteger(130, random).toString(32);
    }


    public final boolean isAuthenticated(String token) {
        return map.containsKey(token);
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
                throw new IllegalStateException(String.format("key=%s is not set", key));
            }
        } else {
            throw new NotAuthorizedException("Not authorized: getObject");
        }
    }
}
