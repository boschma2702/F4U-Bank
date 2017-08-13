package com.bank.service.systemvariables;

import org.springframework.stereotype.Service;
import static com.bank.util.systemvariable.SystemVariableNames.*;

@Service
public class SystemVariableApplyService {

    public void applySystemVariable(String key, String value){
        //TODO implement this
        switch (key){
            case CREDIT_CARD_MONTHLY_FEE:
                break;
            case CREDIT_CARD_DEFAULT_CREDIT:
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
