package com.bank.util.systemvariable;

import com.bank.bean.systemvariables.SystemVariableBean;

public class SystemVariableFieldChecker {

    public static boolean hasObject(String key) {
        try {
            SystemVariableBean.class.getDeclaredField(key);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

}
