package com.bank.service.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.service.transaction.TransactionCreditCardService;
import com.bank.service.transaction.TransactionService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardCloseService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private TransactionService transactionService;

    public void closeCreditCard(int accountId) throws InvalidParamValueException {
        Logger.info("Closing credit card of accountId=%s", accountId);
        CreditCardBean creditCardBean = creditCardService.getCreditCardBeanByAccountId(accountId, false);
        if(creditCardBean.getCredit().compareTo(creditCardBean.getCreditLimit()) != 0){
            Logger.info("Paying of credit card debt of accountId=%s", accountId);
            String message = "Pay off credit card";
            transactionService.retrieveTransaction(creditCardBean.getAccountBean(), creditCardBean.getCreditLimit().subtract(creditCardBean.getCredit()), message);
        }
        creditCardBean.setActive(false);
        creditCardBean.setCredit(creditCardBean.getCreditLimit());
        creditCardRepository.save(creditCardBean);
    }

}
