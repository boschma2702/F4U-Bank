package com.bank.bean.acountsavings;

import com.bank.bean.account.AccountBean;
import com.bank.service.time.TimeService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "account_saving")
public class AccountSavingBean {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_saving_id")
    private int accountSavingId;

    @OneToOne
    @JoinColumn(name = "account_id")
    private AccountBean accountBean;

    private double amount;

    @Column(name = "build_up_interest")
    private double buildUpInterest;

    @Column(name = "creation_date")
    private Date creationDate;


    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBuildUpInterest() {
        return buildUpInterest;
    }

    public void setBuildUpInterest(double buildUpInterest) {
        this.buildUpInterest = buildUpInterest;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @PrePersist
    public void setDate(){
        creationDate = TimeService.TIMESIMULATOR.getCurrentDate();
    }

}
