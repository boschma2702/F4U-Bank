package com.bank.bean.account;

import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.service.time.TimeService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The Account class contains all data values related to a bank account.
 */
@Entity
@Table(name = "account")
public class AccountBean {
    /**
     * Internal account id, unique identifier.
     */
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int accountId;

    /**
     * Account id, unique string.
     */
    @Column(name = "account_number", unique = true)
    private String accountNumber;

    /**
     * Current balance on the account.
     */
    @JsonIgnore
    private double amount;

    /**
     * Indicates whether the account is active or not.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    /**
     * List of customers that are owner of the account.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "accountBean")
    private List<CustomerAccount> customers;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "overdraft_limit")
    private double overdraftLimit = 0;

    @Column(name = "minimum_day_Amount")
    private double minimumDayAmount = 0;

    @Column(name = "build_up_overdraft_interest")
    private double buildUpOverdraftInterest = 0;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<CustomerAccount> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerAccount> customers) {
        this.customers = customers;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public double getMinimumDayAmount() {
        return minimumDayAmount;
    }

    public void setMinimumDayAmount(double minimumDayAmount) {
        this.minimumDayAmount = minimumDayAmount;
    }

    public double getBuildUpOverdraftInterest() {
        return buildUpOverdraftInterest;
    }

    public void setBuildUpOverdraftInterest(double buildUpOverdraftInterest) {
        this.buildUpOverdraftInterest = buildUpOverdraftInterest;
    }

    @PrePersist
    public void setDate(){
        creationDate = TimeService.TIMESIMULATOR.getCurrentDate();
    }
}
