package com.bank.projection.account;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AccountOpenProjection {

    private String iBAN;
    private String pinCard;
    private String pinCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date expirationDate;


    public String getiBAN() {
        return iBAN;
    }

    public void setiBAN(String iBAN) {
        this.iBAN = iBAN;
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
