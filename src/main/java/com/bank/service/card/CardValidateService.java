package com.bank.service.card;

import com.bank.bean.card.CardBean;
import com.bank.exception.InvalidPINException;
import com.bank.repository.card.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardValidateService {
    @Autowired
    private CardRepository cardRepository;

    public CardBean validateCard(int accountId, String pinCard, String pinCode) throws InvalidPINException {
        CardBean bean = cardRepository.getCardBean(accountId, pinCard);
        if (bean == null) {
            throw new InvalidPINException("Invalid pin information");
        }

        if(bean.getPinCode().equals(pinCode)){
            if(bean.getAttempts()!=0){
                bean.setAttempts(0);
                cardRepository.save(bean);
            }
        }else{
            if(bean.getAttempts()==3){
                bean.setActive(false);
            }else {
                bean.setAttempts(bean.getAttempts()+1);
            }
            cardRepository.save(bean);
            throw new InvalidPINException("Invalid pin information");
        }

        return bean;
    }
}
