package com.bank.service.transaction;


import com.bank.bean.account.AccountBean;
import com.bank.bean.transaction.TransactionBean;
import com.bank.repository.account.AccountRepository;
import com.bank.repository.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
@Deprecated
public class TransactionRevertService {

//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    public void resetTransactions(List<TransactionBean> transactionBeans){
//        for(TransactionBean bean : transactionBeans){
//            resetTransaction(bean);
//        }
//    }
//
//    public void resetTransaction(TransactionBean transactionBean){
//        AccountBean sourceBean = transactionBean.getSourceBean();
//        AccountBean targetBean = transactionBean.getTargetBean();
//        double amount = transactionBean.getAmount();
//        sourceBean.setAmount(sourceBean.getAmount()+amount);
//        targetBean.setAmount(targetBean.getAmount()-amount);
//        accountRepository.save(sourceBean);
//        accountRepository.save(targetBean);
//        transactionRepository.delete(transactionBean);
//    }

}
