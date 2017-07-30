package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.exception.NotAuthorizedException;
import com.bank.repository.account.AccountRepository;
import com.bank.repository.card.CardRepository;
import com.bank.repository.customeraccount.CustomerAccountRepository;
import com.bank.service.customer.CustomerCloseService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountCloseService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private CustomerCloseService customerCloseService;

    public void closeAccount(String iBAN, int customerId) throws NotAuthorizedException {
        Logger.info("Closing account accountNumber=%s of customerId=%s", iBAN, customerId);
        // close account
        AccountBean accountBean = accountRepository.findAccountBeanByAccountNumber(iBAN);
        if(accountBean.getAmount().compareTo(BigDecimal.ZERO)<0){
            Logger.error(String.format("Could not close account due to negative balance of accountNumber=%s of customerId=%s", iBAN, customerId));
            throw new NotAuthorizedException("Account has a negative balance");
        }
        accountRepository.closeAccount(iBAN);

        // invalidate all pin cards
        int accountId = accountRepository.findAccountBeanByAccountNumber(iBAN).getAccountId();
        Logger.info("Invalidating pin cards of accountId=%s", accountId);
        cardRepository.invalidatePinCards(accountId);

        // if last account of customer, also invalidate customer
        List<CustomerAccount> customerAccounts = customerAccountRepository.getActiveCustomerAcounts(customerId);
        if (customerAccounts.size() == 0) {
            Logger.info("customerId=%s has no more active accounts", customerId);
            customerCloseService.closeCustomer(customerId);
        }
    }
}
