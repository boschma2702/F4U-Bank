package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.bean.card.CardBean;
import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.repository.account.AccountRepository;
import com.bank.repository.customeraccount.CustomerAccountRepository;
import com.bank.service.IBANGeneratorService;
import com.bank.service.card.CardCreateService;
import com.bank.service.customer.CustomerService;
import com.bank.service.systemvariables.SystemVariableRetrieveService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.bank.util.systemvariable.SystemVariableNames.WEEKLY_TRANSFER_LIMIT;

@Service
public class AccountCreateService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IBANGeneratorService ibanGeneratorService;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private CardCreateService cardCreateService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemVariableRetrieveService systemVariableRetrieveService;

    /**
     * Creates a new account and links it to the given customerAccount. It also creates a new pin and assigns it.
     *
     * @param customerId is the customer id.
     * @param isMain     sets whether the given customerAccount is the main owner.
     */
    public CardBean createAccount(int customerId, boolean isMain) {
        return createAccount(customerId, isMain, false);
    }

    public CardBean createAccount(int customerId, boolean isMain, boolean isMinor){
        Logger.info("Creating an account for customerId=%s and isMain=%s", customerId, isMain);
        AccountBean accountBean = new AccountBean();
        accountBean.setAccountNumber(ibanGeneratorService.generateIBAN());
        accountBean.setMinorAccount(isMinor);
        accountBean.setTransferLimit((BigDecimal) systemVariableRetrieveService.getObjectInternally(WEEKLY_TRANSFER_LIMIT));
        accountRepository.save(accountBean);

        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setAccountId(accountBean.getAccountId());
        customerAccount.setCustomerId(customerId);

        customerAccount.setMain(isMain);

        customerAccountRepository.save(customerAccount);

        // create pin
        return cardCreateService.addCard(customerService.getCustomerBeanById(customerId), accountBean);
    }
}
