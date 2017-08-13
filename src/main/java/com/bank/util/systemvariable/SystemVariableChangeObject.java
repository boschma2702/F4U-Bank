package com.bank.util.systemvariable;

import java.sql.Date;

public class SystemVariableChangeObject implements Comparable<SystemVariableChangeObject> {

    private String key;
    private String value;
    private Date effectDate;

    public SystemVariableChangeObject(String key, String value, Date effectDate) {
        this.key = key;
        this.value = value;
        this.effectDate = effectDate;
    }

    @Override
    public int compareTo(SystemVariableChangeObject o) {
        if(this.effectDate.after(o.getEffectDate())){
            return 1;
        }
        return -1;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Date getEffectDate() {
        return effectDate;
    }
}
