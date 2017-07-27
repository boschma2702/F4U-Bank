package com.bank.util;

import java.math.BigDecimal;

public class AmountFormatter {

    public static double format(double amount){
        BigDecimal decimal = new BigDecimal(amount);
        decimal = decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return decimal.doubleValue();
    }
}
