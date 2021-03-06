package com.bank.service.card;

import com.bank.bean.account.AccountBean;
import com.bank.bean.card.CardBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.repository.card.CardRepository;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.service.time.TimeService;
import com.bank.util.RandomStringGenerator;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

import static com.bank.util.systemvariable.SystemVariableNames.CARD_EXPIRATION_LENGTH;

@Service
public class CardCreateService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardPinCardGenerator cardPinCardGenerator;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    /**
     * Adds a new card to the given account and links it to the given person.
     * The pinCard and pinCode are generated at random
     *
     * @param customerBean the customer
     * @param accountBean  the account
     * @return The cardBean
     */
    @Transactional
    public CardBean addCard(CustomerBean customerBean, AccountBean accountBean) {
        return addCard(customerBean, accountBean, RandomStringGenerator.generateRandomIntegerString(4));
    }

    @Transactional
    public CardBean addCard(CustomerBean customerBean, AccountBean accountBean, String pinCode) {
        Logger.info("Adding card to CustomerBeanId=$s and AccountBeanId=%s", customerBean.getCustomerId(), accountBean.getAccountId());
        CardBean bean = new CardBean();
        bean.setPinCard(cardPinCardGenerator.generatePinCard(accountBean.getAccountId()));
        bean.setPinCode(pinCode);
        bean.setAccountBean(accountBean);
        bean.setCustomerBean(customerBean);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeService.TIMESIMULATOR.getCurrentDate());
        calendar.add(Calendar.YEAR, (Integer) systemVariableRetrieveService.getObjectInternally(CARD_EXPIRATION_LENGTH));
        bean.setExperationDate(calendar.getTime());
        cardRepository.save(bean);
        return bean;
    }
}
