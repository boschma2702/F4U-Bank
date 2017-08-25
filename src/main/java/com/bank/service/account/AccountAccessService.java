package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.bean.card.CardBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.projection.customer.CustomerUsernameProjection;
import com.bank.projection.pin.PinProjection;
import com.bank.repository.account.AccountRepository;
import com.bank.service.card.CardCreateService;
import com.bank.service.customer.CustomerService;
import com.bank.service.customeraccount.CustomerAccountService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountAccessService {
    @Autowired
    private CardCreateService cardCreateService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAccountService customerAccountService;

    @Autowired
    private AccountRepository accountRepository;

    public PinProjection provideAccess(String accountNumber, String username) throws NoEffectException, InvalidParamValueException, AccountFrozenException {
        Logger.info("Providing access to accountNumber=%s to username=%s", accountNumber, username);

        CustomerBean customerBean = customerService.getCustomerBeanByUsername(username);
        AccountBean accountBean = accountService.getAccountBeanByAccountNumber(accountNumber);
        if (customerBean == null || accountBean == null) {
            Logger.error("Could not find customerBean with username=%s or accountBean with accountNumber=%s", username, accountNumber);
            throw new InvalidParamValueException("Invalid account or username");
        }
        if (customerBean.isFrozen() || accountBean.isFrozen()) {
            Logger.error("Could not provide access, accountNumber=%s or username=%s is frozen", accountNumber, username);
            throw new AccountFrozenException("Account or customer frozen");
        }
        // link customer to account
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setMain(false);
        customerAccount.setCustomerBean(customerBean);
        customerAccount.setAccountBean(accountBean);
        customerAccount.setCustomerId(customerBean.getCustomerId());
        customerAccount.setAccountId(accountBean.getAccountId());
        customerAccountService.addCustomerAccount(customerAccount);

        // add pin to customer
        CardBean cardBean = cardCreateService.addCard(customerBean, accountBean);

        PinProjection pinProjection = new PinProjection();
        pinProjection.setPinCard(cardBean.getPinCard());
        pinProjection.setPinCode(cardBean.getPinCode());
        pinProjection.setExpirationDate(cardBean.getExperationDate());

        return pinProjection;
    }

    public void revokeAccess(int customerId, int accountId) throws InvalidParamValueException, NoEffectException, AccountFrozenException {
        Logger.info("Revoking access to accountNumber=%s of customerId=%s", accountId, customerId);
        CustomerBean customerBean = customerService.getCustomerBeanById(customerId);
        AccountBean accountBean = accountService.getAccountBeanByAccountId(accountId);
        if (customerBean == null || accountBean == null) {
            Logger.error("Could not find customerBean with customerId=%s or accountBean with accountId=%s", customerId, accountId);
            throw new InvalidParamValueException("Invalid account or username");
        }
        if (customerBean.isFrozen() || accountBean.isFrozen()) {
            Logger.error("Could not revoke access, account or customer is frozen");
            throw new AccountFrozenException("Account or customer is frozen");
        }
        customerAccountService.removeCustomerAccount(customerId, accountBean.getAccountId());
    }

    public List<CustomerUsernameProjection> getBankAccountAccess(int accountId) {
        Logger.info("Retrieving customerUsernameProjections from accountId=%s", accountId);
        return accountRepository.getBankAccountAccess(accountId);
    }
}
