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
        String creditCardNumber = generateCreditCardNumberCheckDigit(PREFIX + RandomStringGenerator.generateRandomIntegerString(9));
        return creditCardRepository.isCreditCardNumberTaken(creditCardNumber) ? generateCreditCardNumber() : creditCardNumber;
    }

    private String generateCreditCardNumberCheckDigit(String creditCardNumber){
        char[] charArray = creditCardNumber.toCharArray();
        boolean doubleValue = true;
        int sum = 0;
        for(int i=charArray.length-1; i>=0; i--){
            int number = Character.getNumericValue(charArray[i]);
            if(doubleValue){
                number = number*2;
                number = number>9 ? number-9 : number;
            }
            sum += number;
            doubleValue = !doubleValue;
        }
        sum = sum*9;
        int checkDigit = sum % 10;
        return creditCardNumber+checkDigit;
    }
}
