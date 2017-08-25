package com.bank.bean.customer;

import com.bank.service.time.TimeService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;

/**
 * The Customer class contains all relevant data values related to a customer.
 */
@Entity
@Table(name = "customer")
public class CustomerBean {
    /**
     * Internal customer id.
     */
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", insertable = false)
    private int customerId;

    /**
     * First name of the customer
     */
    private String name;
    /**
     * Last name of the customer
     */
    private String surname;
    /**
     * Initials of the customer
     */
    private String initials;
    /**
     * Date-of-birth of the customer
     */
    private Date dob;
    /**
     * ssn of the customer
     */
    private String ssn;

    /**
     * Full street, house number and optional additions to the house number, currently no validation is implemented.
     */
    private String address;
    /**
     * Phone number of the customer, currently no validation is implemented.
     */
    @Column(name = "telephone_number")
    private String telephoneNumber;

    /**
     * Email address of the customer, currently no validation is implemented.
     */
    private String email;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "creation_date")
    private java.util.Date creationDate;

    private boolean frozen = false;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    @PrePersist
    public void setDate() {
        creationDate = TimeService.TIMESIMULATOR.getCurrentDate();
    }
}