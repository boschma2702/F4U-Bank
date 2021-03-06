package com.bank.bean.card;

import com.bank.bean.account.AccountBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.service.time.TimeService;
import com.bank.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The Card class contains all data values related to a card.
 */
@Entity
@Table(name = "card")
public class CardBean {
    /**
     * Internal card id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "card_id")
    private int cardId;

    /**
     * The number on the card.
     */
    @Column(name = "pin_card")
    private String pinCard;

    @Column(name = "pin_code")
    private String pinCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerBean customerBean;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountBean accountBean;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    private int attempts = 0;

    @Column(name = "limit_day", scale = 2)
    private BigDecimal dayLimit = Constants.CARD_DAY_LIMIT;

    @Column(name = "limit_day_remaining", scale = 2)
    private BigDecimal dayLimitRemaining = Constants.CARD_DAY_LIMIT;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "expiration_date")
    private Date experationDate;

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getPinCard() {
        return pinCard;
    }

    public void setPinCard(String pinCard) {
        this.pinCard = pinCard;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(CustomerBean customerBean) {
        this.customerBean = customerBean;
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

    public Date getExperationDate() {
        return experationDate;
    }

    public void setExperationDate(Date experationDate) {
        this.experationDate = experationDate;
    }

    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    public BigDecimal getDayLimitRemaining() {
        return dayLimitRemaining;
    }

    public void setDayLimitRemaining(BigDecimal dayLimitRemaining) {
        this.dayLimitRemaining = dayLimitRemaining;
    }

    @PrePersist
    public void setDate() {
        creationDate = TimeService.TIMESIMULATOR.getCurrentDate();
    }
}
