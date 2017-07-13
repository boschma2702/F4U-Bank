package com.bank.bean.systeminfo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "system_info")
public class SystemInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "time_diff")
    private long timeDiff;

    @Column(name = "initial_date")
    private Date initialDate;


    public long getTimeDiff() {
        return timeDiff;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public void setTimeDiff(long timeDiff) {

        this.timeDiff = timeDiff;
    }
}
