package com.bank.service.transaction;

import com.bank.bean.transaction.TransactionBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.transaction.TransactionProjection;
import com.bank.repository.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionOverviewService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<TransactionProjection> getTransactionOverview(int customerId, int amount) throws InvalidParamValueException {
        try {
            Page<TransactionBean> page = transactionRepository.getListOfXLatestTransactions(new PageRequest(0, amount), customerId);
            List<TransactionBean> list = page.getContent();
            List<TransactionProjection> projectionList = new ArrayList<>();
            for (TransactionBean transactionBean : list) {
                String sourceIBAN;
                if (transactionBean.getCreditCardBean() == null) {
                    sourceIBAN = transactionBean.getSourceBean() != null ? transactionBean.getSourceBean().getAccountNumber() : null;
                } else {
                    sourceIBAN = transactionBean.getCreditCardBean().getAccountBean().getAccountNumber() + "C";
                }
                String targetIBAN = transactionBean.getTargetBean() != null ? transactionBean.getTargetBean().getAccountNumber() : null;
                projectionList.add(new TransactionProjection(sourceIBAN, targetIBAN, transactionBean.getTargetName(), transactionBean.getDate(), transactionBean.getAmount(), transactionBean.getComment(), transactionBean.isFromSavings()));
            }
            return projectionList;
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueException("Invalid nrOfTransactions");
        }
    }

}
