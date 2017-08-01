package com.bank.bean.person;

import com.bank.bean.customer.CustomerBean;
import com.bank.bean.employee.EmployeeBean;
import com.bank.service.time.TimeService;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "person", uniqueConstraints = @UniqueConstraint(columnNames = {"user_name"}))
public class PersonBean {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;

    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "creation_date")
    private Date creationDate;

    @OneToOne
    @JoinColumn(name = "customer_id")
    @Cascade(CascadeType.ALL)
    private CustomerBean customerBean;

    @OneToOne
    @JoinColumn(name = "employee_id")
    @Cascade(CascadeType.ALL)
    private EmployeeBean employeeBean;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(CustomerBean customerBean) {
        this.customerBean = customerBean;
    }

    public EmployeeBean getEmployeeBean() {
        return employeeBean;
    }

    public void setEmployeeBean(EmployeeBean employeeBean) {
        this.employeeBean = employeeBean;
    }

    @PrePersist
    public void setDate(){
        creationDate = TimeService.TIMESIMULATOR.getCurrentDate();
    }

}
