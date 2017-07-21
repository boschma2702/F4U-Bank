package com.bank.service.card;

import com.bank.bean.card.CardBean;
import com.bank.exception.NoEffectException;
import com.bank.repository.card.CardRepository;
import com.bank.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardUnblockService {

    @Autowired
    private CardRepository cardRepository;

    /**
     * Unblocks the given card assigned to the given card. The card must be blocked and belong to an active account in
     * order to have effect.
     * @param accountId id of the account
     * @param pinCard pin card number
     * @throws NoEffectException card was not blocked or belongs to an inactive account
     */
    public void unblockCard(int accountId, String pinCard) throws NoEffectException {
        Logger.info("Unblocking pinCard=%s of accountId=%s", pinCard, accountId);
        CardBean bean = cardRepository.getBlockedCardOfNonBlockedAccount(accountId, pinCard);
        if(bean == null){
            Logger.error("Could not find blocked pinCard=%s of accountId=%s", pinCard, accountId);
            throw new NoEffectException("Blocked card not present");
        }
        bean.setAttempts(0);
        bean.setActive(true);
        cardRepository.save(bean);
    }
}
