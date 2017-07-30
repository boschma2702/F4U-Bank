package com.bank.service;

import com.bank.repository.account.AccountRepository;
import com.bank.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IBANGeneratorService {
    private static final String PREFIX = "NL69F4UB";
    private static final int RANDOM_SUFFIX_SIZE = 10;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Produces a random IBAN not conform to the standard. This function does also not guarantee
     * that every generated IBAN is unique
     *
     * @return the generated IBAN number
     */
    public String generateIBAN() {
        String IBAN = PREFIX + RandomStringGenerator.generateRandomIntegerString(RANDOM_SUFFIX_SIZE);
        IBAN = (accountRepository.isAccountNumberTaken(IBAN) ? generateIBAN() : IBAN);
        return IBAN;
    }
}
