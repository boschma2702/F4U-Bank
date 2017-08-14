package com.bank.service.creditcard;

import com.bank.bean.account.AccountBean;
import com.bank.bean.creditcard.CreditCardBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.pin.CardProjection;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.service.transaction.TransactionService;
import com.bank.util.Constants;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.bank.util.systemvariable.SystemVariableNames.NEW_CARD_COST;

@Service
public class CreditCardInvalidateService {


    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CreditCardCreateService creditCardCreateService;

    @Autowired
    private CreditCardCloseService creditCardCloseService;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    @Transactional
    public CardProjection invalidateCard(String creditCardNumber, boolean newPin) throws InvalidParamValueException {
        Logger.info("Invalidating creditCardNumber=%s", creditCardNumber);
        CreditCardBean creditCardBean = creditCardService.getCreditCardBeanByCreditCardNumber(creditCardNumber, false);
        AccountBean accountBean = creditCardBean.getAccountBean();

        creditCardCloseService.closeCreditCard(accountBean.getAccountId());
        transactionService.retrieveTransaction(accountBean, (BigDecimal) systemVariableRetrieveService.getObjectInternally(NEW_CARD_COST), "Credit card replacement costs");

        CardProjection newCardProjection = newPin ? creditCardCreateService.createCreditCard(accountBean.getAccountId()) : creditCardCreateService.createCreditCard(accountBean.getAccountId(), creditCardBean.getCreditCardPin());

        if(!newPin){
            newCardProjection.setPinCode(null);
        }
        return newCardProjection;
    }


}
