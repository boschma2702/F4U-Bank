package com.bank.bean.creditcard;

import com.bank.bean.account.AccountBean;
import com.bank.service.time.TimeService;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "credit_card", uniqueConstraints = @UniqueConstraint(columnNames = {"credit_card_number"}))
public class CreditCardBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_card_id")
    private int creditCardId;

    @Column(name = "credit_card_number")
    private String creditCardNumber;

    @Column(name = "credit_card_pin", nullable = false)
    private String creditCardPin;

    @Column(scale = 2)
    private BigDecimal credit = new BigDecimal(1000);

    @Column(name = "credit_limit", scale = 2)
    private BigDecimal creditLimit = new BigDecimal(1000);

    @OneToOne()
    @JoinColumn(name = "account_id")
    private AccountBean accountBean;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "activation_date")
    private Date activationDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "expiration_date")
    private Date expirationDate;

    private int attempts = 0;

    public CreditCardBean() {
    }

    public int getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(int creditCardId) {
        this.creditCardId = creditCardId;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreditCardPin() {
        return creditCardPin;
    }

    public void setCreditCardPin(String creditCardPin) {
        this.creditCardPin = creditCardPin;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    @PrePersist
    public void setDate(){
        creationDate = TimeService.TIMESIMULATOR.getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        activationDate = calendar.getTime();
    }

}
