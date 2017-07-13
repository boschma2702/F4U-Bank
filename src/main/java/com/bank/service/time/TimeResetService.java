package com.bank.service.time;

import com.bank.bean.account.AccountBean;
import com.bank.bean.systeminfo.SystemInfo;
import com.bank.bean.transaction.TransactionBean;
import com.bank.exception.NoEffectException;
import com.bank.repository.account.AccountRepository;
import com.bank.repository.card.CardRepository;
import com.bank.repository.customer.CustomerRepository;
import com.bank.repository.customeraccount.CustomerAccountRepository;
import com.bank.repository.systeminfo.SystemInfoRepository;
import com.bank.repository.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class TimeResetService {

    @Autowired
    private TimeService timeService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SystemInfoRepository systemInfoRepository;

    @Transactional
    public void reset() throws NoEffectException {
        SystemInfo systemInfo = timeService.getSystemInfo();
        resetTransactions(systemInfo.getInitialDate());
        resetCustomerAccounts(systemInfo.getInitialDate());
        resetCards(systemInfo.getInitialDate());
        resetAccounts(systemInfo.getInitialDate());
        resetCustomers(systemInfo.getInitialDate());
        resetSystemInfo();
    }


    private void resetTransactions(Date date){
        List<TransactionBean> transactionBeans = transactionRepository.findTransactionBeansByDateAfter(date);
        for(TransactionBean bean : transactionBeans){
            AccountBean sourceBean = bean.getSourceBean();
            AccountBean targetBean = bean.getTargetBean();
            double amount = bean.getAmount();
            sourceBean.setAmount(sourceBean.getAmount()+amount);
            targetBean.setAmount(targetBean.getAmount()-amount);
            accountRepository.save(sourceBean);
            accountRepository.save(targetBean);
            transactionRepository.delete(bean);
        }
    }

    private void resetCustomerAccounts(Date date){
        customerAccountRepository.deleteCustomerAccountsByCreationDateAfter(date);
    }

    private void resetCards(Date date){
        cardRepository.deleteCardBeansByCreationDateAfter(date);
    }

    private void resetAccounts(Date date){
        accountRepository.deleteAccountBeansByCreationDateAfter(date);
    }

    private void resetCustomers(Date date){
        customerRepository.deleteCustomerBeansByCreationDateAfter(date);
    }

    private void resetSystemInfo(){
        systemInfoRepository.deleteAll();
    }

}
