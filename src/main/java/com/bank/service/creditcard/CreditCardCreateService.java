package com.bank.service.creditcard;

import com.bank.bean.account.AccountBean;
import com.bank.bean.creditcard.CreditCardBean;
import com.bank.projection.pin.CardProjection;
import com.bank.repository.account.AccountRepository;
import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardCreateService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardNumberGenerator creditCardNumberGenerator;

    public CardProjection createCreditCard(int accountId){
        AccountBean accountBean = accountRepository.findAccountBeansByAccountId(accountId);
        CreditCardBean creditCardBean = new CreditCardBean();
        creditCardBean.setCreditCardNumber(creditCardNumberGenerator.generateCreditCardNumber());
        creditCardBean.setCreditCardPin(RandomStringGenerator.generateRandomIntegerString(4));
        creditCardBean.setAccountBean(accountBean);
        creditCardRepository.save(creditCardBean);

        CardProjection cardProjection = new CardProjection();
        cardProjection.setPinCard(creditCardBean.getCreditCardNumber());
        cardProjection.setPinCode(creditCardBean.getCreditCardPin());
        return cardProjection;
    }


}
