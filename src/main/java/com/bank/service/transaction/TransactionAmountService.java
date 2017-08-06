package com.bank.service.transaction;

import com.bank.repository.transaction.TransactionRepository;
import com.bank.service.time.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;

@Service
public class TransactionAmountService {

    @Autowired
    private TransactionRepository transactionRepository;

    public BigDecimal getTransactionAmountSinceDays(int accountId, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeService.TIMESIMULATOR.getCurrentDate());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        BigDecimal amount = transactionRepository.getTransferredAmountSince(accountId, calendar.getTime());
        if(amount == null){
            return BigDecimal.ZERO;
        }
        return amount;
    }

}
