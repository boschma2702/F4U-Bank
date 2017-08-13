package com.bank.service.systemvariables;

import com.bank.repository.creditcard.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.bank.util.systemvariable.SystemVariableNames.*;

@Service
public class SystemVariableApplyService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    public void applySystemVariable(String key, String value, Object oldValue){
        switch (key){
            case CREDIT_CARD_MONTHLY_FEE:
                //No further change needed
                break;
            case CREDIT_CARD_DEFAULT_CREDIT:
                creditCardRepository.setCreditCardLimit((BigDecimal) oldValue, (BigDecimal) systemVariableRetrieveService.getObjectInternally(key));
                break;
            case CARD_EXPIRATION_LENGTH:
                break;
            case NEW_CARD_COST:
                break;
            case CARD_USAGE_ATTEMPTS:
                break;
            case MAX_OVERDRAFT_LIMIT:
                break;
            case INTEREST_RATE_1:
                break;
            case INTEREST_RATE_2:
                break;
            case INTEREST_RATE_3:
                break;
            case OVERDRAFT_INTEREST_RATE:
                break;
            case DAILY_WITHDRAW_LIMIT:
                break;
            case WEEKLY_TRANSFER_LIMIT:
                break;
        }
    }


}
