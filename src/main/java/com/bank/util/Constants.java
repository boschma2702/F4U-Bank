package com.bank.util;

import java.math.BigDecimal;

public class Constants {

//    public static final int CARD_BLOCK_LIMIT = 3;


    public static final BigDecimal CARD_DAY_LIMIT = new BigDecimal(250);
    public static final int AGE_MINOR_MAX = 18;
    public static final String ACCOUNT_TYPE_MINOR = "child";
    public static final String ACCOUNT_TYPE_REGULAR = "regular";
    //    public static final BigDecimal ACCOUNT_DEFAULT_TRANSFER_LIMIT = new BigDecimal(2500);
    public static int TRANSACTION_DAYS_LIMIT = 6;
}
