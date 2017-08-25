package com.bank.bean.transaction;

import com.bank.bean.account.AccountBean;
import com.bank.bean.card.CardBean;
import com.bank.bean.creditcard.CreditCardBean;
import com.bank.service.time.TimeService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class TransactionBean {


    /**
     * Internal transaction id, unique identifier.
     */
    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int transactionId;

    /**
     * Amount to be transferred to the target account.
     */
    @Column(scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id")
    private AccountBean sourceBean;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_account_id")
    private AccountBean targetBean;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private CardBean card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_id")
    private CreditCardBean creditCardBean;

    private Date date;

    @Column(name = "target_name")
    private String targetName = "";

    /**
     * Custom comment a customer can give with a transaction.
     */
    @Column(length = 128)
    private String comment;

    @Column(name = "from_savings")
    private boolean fromSavings = false;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AccountBean getSourceBean() {
        return sourceBean;
    }

    public void setSourceBean(AccountBean sourceBean) {
        this.sourceBean = sourceBean;
    }

    public AccountBean getTargetBean() {
        return targetBean;
    }

    public void setTargetBean(AccountBean targetBean) {
        this.targetBean = targetBean;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CardBean getCard() {
        return card;
    }

    public void setCard(CardBean card) {
        this.card = card;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public boolean isFromSavings() {
        return fromSavings;
    }

    public void setFromSavings(boolean fromSavings) {
        this.fromSavings = fromSavings;
    }

    public CreditCardBean getCreditCardBean() {
        return creditCardBean;
    }

    public void setCreditCardBean(CreditCardBean creditCardBean) {
        this.creditCardBean = creditCardBean;
    }

    @PrePersist
    public void setDate() {
        date = TimeService.TIMESIMULATOR.getCurrentDate();
    }
}
