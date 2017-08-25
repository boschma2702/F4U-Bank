package com.bank.util.systemvariable;

import java.math.BigDecimal;
import java.sql.Date;

public class SystemVariableChangeObject implements Comparable<SystemVariableChangeObject> {

    private String key;
    private BigDecimal value;
    private Date effectDate;

    public SystemVariableChangeObject(String key, BigDecimal value, Date effectDate) {
        this.key = key;
        this.value = value;
        this.effectDate = effectDate;
    }

    @Override
    public int compareTo(SystemVariableChangeObject o) {
        if (this.effectDate.after(o.getEffectDate())) {
            return 1;
        }
        return -1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }
}
