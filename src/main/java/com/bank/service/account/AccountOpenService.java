package com.bank.service.account;

import com.bank.bean.card.CardBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.bean.person.PersonBean;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.exception.NoEffectException;
import com.bank.exception.NotAuthorizedException;
import com.bank.projection.account.AccountOpenProjection;
import com.bank.service.customer.CustomerCreateService;
import com.bank.service.customer.CustomerService;
import com.bank.util.AgeChecker;
import com.bank.util.Constants;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@Service
public class AccountOpenService {
    @Autowired
    private CustomerCreateService customerCreateService;

    @Autowired
    private AccountCreateService accountCreateService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountAccessService accountAccessService;

    /**
     * Opens an account. This creates a new customer, a new account and a new pin card.
     *
     * @param name
     * @param surname
     * @param initials
     * @param date
     * @param ssn
     * @param address
     * @param telephoneNumber
     * @param email
     * @param username
     * @param password
     * @return projection containing the account id, pin number and pin code.
     * @throws InvalidParamValueException when parameters are not correct.
     */
    @Transactional(rollbackFor = Exception.class)
    public AccountOpenProjection openAccount(String name,
                                             String surname,
                                             String initials,
                                             Date date,
                                             String ssn,
                                             String address,
                                             String telephoneNumber,
                                             String email,
                                             String username,
                                             String password,
                                             String type,
                                             String[] guardians) throws InvalidParamValueException, NotAuthorizedException, AccountFrozenException {

        Logger.info("Opening an account for name=%s, surname=%s and username=%s", name, surname, username);
        boolean isMinor = AgeChecker.isMinor(date);
        if (type.equals(Constants.ACCOUNT_TYPE_MINOR) != isMinor) {
            Logger.error("Could not open minor account for name=%s, surname=%s and username=%s, account type does not match given age", name, surname, username);
            throw new InvalidParamValueException("Given age and type do not match");
        }
        if (isMinor && guardians.length < 1) {
            Logger.error("Could not open account for name=%s, surname=%s and username=%s, not enough guardians specified", name, surname, username);
            throw new InvalidParamValueException("Not enough guardians specified");
        }

        CustomerBean customerBean = new CustomerBean();
        customerBean.setName(name);
        customerBean.setSurname(surname);
        customerBean.setInitials(initials);
        customerBean.setDob(date);
        customerBean.setSsn(ssn);
        customerBean.setAddress(address);
        customerBean.setTelephoneNumber(telephoneNumber);
        customerBean.setEmail(email);

        PersonBean personBean = new PersonBean();
        personBean.setUsername(username);
        personBean.setPassword(password);
        personBean.setCustomerBean(customerBean);

        customerCreateService.createCustomer(personBean);

        CardBean cardBean = accountCreateService.createAccount(customerBean.getCustomerId(), true, isMinor);

        for (String guardianUsername : guardians) {
            CustomerBean guardianBean = customerService.getCustomerBeanByUsername(guardianUsername);
            if (AgeChecker.isMinor(guardianBean.getDob())) {
                Logger.error("Could not open account for name=%s, surname=%s and username=%s, guardianUsername=%s does not meet the requirements", name, surname, username, guardianUsername);
                throw new InvalidParamValueException(String.format("Guardian %s does not meet the age requirement", guardianUsername));
            } else {
                try {
                    accountAccessService.provideAccess(cardBean.getAccountBean().getAccountNumber(), guardianUsername);
                } catch (NoEffectException e) {
                    //DO nothing
                } catch (AccountFrozenException e) {
                    Logger.error("Given guardian account is frozen");
                    throw new AccountFrozenException("(One of the) given guardian account frozen");
                }
            }
        }

        AccountOpenProjection projection = new AccountOpenProjection();
        projection.setiBAN(cardBean.getAccountBean().getAccountNumber());
        projection.setPinCard(cardBean.getPinCard());
        projection.setPinCode(cardBean.getPinCode());
        projection.setExpirationDate(cardBean.getExperationDate());
        return projection;
    }

    public AccountOpenProjection openAdditionalAccount(int customerId) throws AccountFrozenException {
        Logger.info("Opening an additional account for customerId=%s", customerId);
        CustomerBean customerBean = customerService.getCustomerBeanById(customerId);
        if (customerBean.isFrozen()) {
            Logger.error("Could not open additional account for customerId=%s", customerId);
            throw new AccountFrozenException("Account frozen");
        }
        CardBean cardBean = accountCreateService.createAccount(customerId, true);
        AccountOpenProjection projection = new AccountOpenProjection();
        projection.setiBAN(cardBean.getAccountBean().getAccountNumber());
        projection.setPinCard(cardBean.getPinCard());
        projection.setPinCode(cardBean.getPinCode());
        projection.setExpirationDate(cardBean.getExperationDate());
        return projection;
    }


}
