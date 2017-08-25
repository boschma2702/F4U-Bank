package com.bank.bean.employee;

import com.bank.service.time.TimeService;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employee")
public class EmployeeBean {

    @Id
    @Column(name = "employee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;

    @Column(name = "creation_date")
    private Date creationDate;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @PrePersist
    public void setDate() {
        creationDate = TimeService.TIMESIMULATOR.getCurrentDate();
    }
}
