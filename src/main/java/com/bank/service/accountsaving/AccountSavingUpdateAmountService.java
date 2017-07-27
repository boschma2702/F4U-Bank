package com.bank.service.accountsaving;

import com.bank.repository.accountsaving.AccountSavingRepository;
import com.bank.util.Logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountSavingUpdateAmountService {

    @Autowired
    private AccountSavingRepository accountSavingRepository;

    public void updateAmount(int accountId, double amount){
        Logger.info("Updating amount=$s of targetAccountId=%s", amount, accountId);
        accountSavingRepository.updateAmount(accountId, amount);
    }

}
