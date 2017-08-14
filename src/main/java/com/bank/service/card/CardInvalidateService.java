package com.bank.service.card;

import com.bank.bean.account.AccountBean;
import com.bank.bean.card.CardBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.pin.CardProjection;
import com.bank.repository.card.CardRepository;
import com.bank.service.account.AccountService;
import com.bank.service.customer.CustomerService;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.service.time.TimeService;
import com.bank.service.transaction.TransactionService;
import com.bank.util.Constants;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.bank.util.systemvariable.SystemVariableNames.NEW_CARD_COST;


@Service
public class CardInvalidateService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardCreateService cardCreateService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    @Transactional
    public CardProjection invalidateCard(int accountId, int customerId, String pinCard, boolean newPin) throws InvalidParamValueException {
        Logger.info("Invalidating pinCard=%s of accountId=%s", pinCard, accountId);
        CardBean cardBean = cardService.getCardBean(pinCard, accountId);
        AccountBean accountBean = accountService.getAccountBeanByAccountId(accountId);
        CustomerBean customerBean = customerService.getCustomerBeanById(customerId);

        transactionService.retrieveTransaction(accountBean, (BigDecimal) systemVariableRetrieveService.getObjectInternally(NEW_CARD_COST), "Card replacement costs");

        CardBean newCard;
        if(newPin){
            newCard = cardCreateService.addCard(customerBean, accountBean);
        }else{
            newCard = cardCreateService.addCard(customerBean, accountBean, cardBean.getPinCode());
        }

        cardRepository.invalidatePinCard(accountId, pinCard, TimeService.TIMESIMULATOR.getCurrentDate());

        CardProjection cardProjection = new CardProjection();
        cardProjection.setPinCard(newCard.getPinCard());
        if(newPin){
            cardProjection.setPinCode(newCard.getPinCode());
        }
        return cardProjection;
    }

}
