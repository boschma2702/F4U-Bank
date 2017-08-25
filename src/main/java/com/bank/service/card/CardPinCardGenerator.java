package com.bank.service.card;

import com.bank.repository.card.CardRepository;
import com.bank.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardPinCardGenerator {

    @Autowired
    private CardRepository cardRepository;

    public String generatePinCard(int accountId) {
        String pinCard = RandomStringGenerator.generateRandomIntegerString(4);
        pinCard = (cardRepository.isPinCardTaken(pinCard, accountId) ? generatePinCard(accountId) : pinCard);
        return pinCard;
    }

}
