package com.bank.service.creditcard;

import com.bank.repository.creditcard.CreditCardRepository;
import com.bank.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardNumberGenerator {

    private static final String PREFIX = "524886";

    @Autowired
    private CreditCardRepository creditCardRepository;

    public String generateCreditCardNumber(){
        String creditCardNumber = PREFIX + RandomStringGenerator.generateRandomIntegerString(10);
        return creditCardRepository.isCreditCardNumberTaken(creditCardNumber) ? generateCreditCardNumber() : creditCardNumber;
    }


}
