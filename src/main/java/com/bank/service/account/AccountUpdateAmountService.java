package com.bank.service.account;

import com.bank.repository.account.AccountRepository;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class AccountUpdateAmountService {
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public void updateAmount(int sourceAccountId, int targetAccountId, BigDecimal amount) {
        Logger.info("Updating amount=%s of sourceAccountId=%s and targetAccountId=%s", amount, sourceAccountId, targetAccountId);
        accountRepository.updateAmount(sourceAccountId, amount.negate());
        accountRepository.updateAmount(targetAccountId, amount);
    }

    public void updateAmount(int targetAccountId, BigDecimal amount) {
        Logger.info("Updating amount=%s of targetAccountId=%s", amount, targetAccountId);
        accountRepository.updateAmount(targetAccountId, amount);
    }
}
