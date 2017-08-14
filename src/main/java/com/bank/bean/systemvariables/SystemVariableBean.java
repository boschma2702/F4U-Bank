package com.bank.bean.systemvariables;

import com.bank.util.systemvariable.Money;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "system_variables")
public class SystemVariableBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(scale = 2)
    @Money
    private BigDecimal CREDIT_CARD_MONTHLY_FEE = new BigDecimal(5);

    @Column(scale = 2)
    @Money
    private BigDecimal CREDIT_CARD_DEFAULT_CREDIT = new BigDecimal(1000);

    private int CARD_EXPIRATION_LENGTH = 4;

    @Column(scale = 2)
    @Money
    private BigDecimal NEW_CARD_COST = new BigDecimal(7.50);

    private int CARD_USAGE_ATTEMPTS = 3;

    @Money
    private double MAX_OVERDRAFT_LIMIT = 5000;

    private double INTEREST_RATE_1 = 0.15e-2;

    private double INTEREST_RATE_2 = 0.20e-2;

    private double INTEREST_RATE_3 = 0;

    private double OVERDRAFT_INTEREST_RATE = 0.1;

    @Column(scale = 2)
    @Money
    private BigDecimal DAILY_WITHDRAW_LIMIT = new BigDecimal(250);

    @Column(scale = 2)
    @Money
    private BigDecimal WEEKLY_TRANSFER_LIMIT = new BigDecimal(2500);

}
