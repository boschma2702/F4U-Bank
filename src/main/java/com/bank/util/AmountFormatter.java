package com.bank.util;

import java.math.BigDecimal;

public class AmountFormatter {

    public static BigDecimal format(double amount){
        BigDecimal decimal = new BigDecimal(amount);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
