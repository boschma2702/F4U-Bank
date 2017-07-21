package com.bank.service.interest;

import com.bank.bean.account.AccountBean;
import com.bank.repository.account.AccountRepository;
import com.bank.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InterestOverdraftTransferService {

    @Autowired
    private AccountRepository accountRepository;

    public void transferOverdraftInterest(){
        Logger.info("Transferring overdraft interest");
        List<AccountBean> accountBeans = accountRepository.getActiveAccountBeansWithOverdraftInterest();
        for(AccountBean accountBean : accountBeans){
            BigDecimal decimal = new BigDecimal(accountBean.getBuildUpOverdraftInterest());
            decimal = decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            accountBean.setAmount(accountBean.getAmount()+decimal.doubleValue());
            accountBean.setBuildUpOverdraftInterest(0);
            Logger.info("Overdraft interest amount=%s transferred to accountId=%s", decimal.doubleValue(), accountBean.getAccountId());
            accountRepository.save(accountBean);
        }
    }

}
