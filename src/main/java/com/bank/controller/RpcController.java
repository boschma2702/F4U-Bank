package com.bank.controller;


import com.bank.exception.*;
import com.bank.util.EmptyJsonResponse;
import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;

@Service
@JsonRpcService("/api")
@AutoJsonRpcServiceImpl
public class RpcController {
    /**
     * Account module
     */
    @Autowired
    private AccountController accountController;
    /**
     * Access module
     */

    @Autowired
    private AccountAccessController accountAccessController;
    /**
     * Transfer module
     */
    @Autowired
    private TransactionController transactionController;
    /**
     * Authentication module
     */
    @Autowired
    private SessionController sessionController;
    /**
     * Info Module
     */
    @Autowired
    private CustomerController customerController;
    /**
     * Extension 2
     */
    @Autowired
    private CardController cardController;
    /**
     * Time extension
     */
    @Autowired
    private TimeController timeController;
    /**
     * Logger extension
     */

    @Autowired
    private LoggingController loggingController;
    /**
     * Savings extension
     */

    @Autowired
    private AccountSavingController accountSavingController;
    /**
     * CreditCard extension
     */

    @Autowired
    private CreditCardController creditCardController;
    /**
     * Administrive user III
     */
    @Autowired
    private SystemVariableController systemVariableController;

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object openAccount(@JsonRpcParam("name") String name,
                              @JsonRpcParam("surname") String surname,
                              @JsonRpcParam("initials") String initials,
                              @JsonRpcParam("dob") Date date,
                              @JsonRpcParam("ssn") String ssn,
                              @JsonRpcParam("address") String address,
                              @JsonRpcParam("telephoneNumber") String telephoneNumber,
                              @JsonRpcParam("email") String email,
                              @JsonRpcParam("username") String username,
                              @JsonRpcParam("password") String password) throws InvalidParamValueException, NotAuthorizedException {
        return accountController.openAccount(name, surname, initials, date, ssn, address, telephoneNumber, email, username, password);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAllowedException.class, code = 424)
    })
    public Object openAdditionalAccount(@JsonRpcParam("authToken") String authToken) throws NotAuthorizedException, AccountFrozenException, NotAllowedException {
        return accountController.openAdditionalAccount(authToken);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object closeAccount(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN) throws InvalidParamValueException, NotAuthorizedException, AccountFrozenException {
        accountController.closeAccount(authToken, iBAN);
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NoEffectException.class, code = 420),
            @JsonRpcError(exception = NotAllowedException.class, code = 424)
    })
    public Object provideAccess(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("username") String username) throws NotAuthorizedException, InvalidParamValueException, NoEffectException, AccountFrozenException, NotAllowedException {
        return accountAccessController.provideAccess(authToken, iBAN, username);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NoEffectException.class, code = 420),
            @JsonRpcError(exception = NotAllowedException.class, code = 424)
    })
    public Object revokeAccess(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("username") String username) throws NotAuthorizedException, InvalidParamValueException, NoEffectException, AccountFrozenException, NotAllowedException {
        accountAccessController.revokeAccess(authToken, iBAN, username);
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NoEffectException.class, code = 420),
            @JsonRpcError(exception = NotAllowedException.class, code = 424)
    })
    public Object revokeAccess(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN) throws NotAuthorizedException, InvalidParamValueException, NoEffectException, AccountFrozenException, NotAllowedException {
        accountAccessController.revokeAccess(authToken, iBAN, null);
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = InvalidPINException.class, code = 421)
    })
    public Object depositIntoAccount(@JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("pinCard") String pinCard, @JsonRpcParam("pinCode") String pinCode, @JsonRpcParam("amount") double amount) throws InvalidParamValueException, InvalidPINException, AccountFrozenException {
        transactionController.depositIntoAccount(iBAN, pinCard, pinCode, new BigDecimal(amount));
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = InvalidPINException.class, code = 421)
    })
    public Object payFromAccount(@JsonRpcParam("sourceIBAN") String sourceIBAN, @JsonRpcParam("targetIBAN") String targetIBAN, @JsonRpcParam("pinCard") String pinCard, @JsonRpcParam("pinCode") String pinCode, @JsonRpcParam("amount") BigDecimal amount) throws InvalidParamValueException, InvalidPINException, AccountFrozenException {
        transactionController.payFromAccount(sourceIBAN, targetIBAN, pinCard, pinCode, amount);
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object transferMoney(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("sourceIBAN") String sourceIBAN, @JsonRpcParam("targetIBAN") String targetIBAN, @JsonRpcParam("targetName") String targetName, @JsonRpcParam("amount") BigDecimal amount, @JsonRpcParam("description") String description) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException {
        transactionController.transferMoney(authToken, sourceIBAN, targetIBAN, targetName, amount, description);
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = AuthenticationException.class, code = 422)
    })
    public Object getAuthToken(@JsonRpcParam("username") String username, @JsonRpcParam("password") String password) throws AuthenticationException {
        return sessionController.getAuthToken(username, password);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object getBalance(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN) throws NotAuthorizedException, InvalidParamValueException {
        return accountController.getBalance(authToken, iBAN);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object getTransactionsOverview(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("nrOfTransactions") int nrOfTransactions) throws InvalidParamValueException, NotAuthorizedException {
        return transactionController.getTransactionsOverview(authToken, iBAN, nrOfTransactions);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object getUserAccess(@JsonRpcParam("authToken") String authToken) throws NotAuthorizedException {
        return customerController.getUserAccess(authToken);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object getBankAccountAccess(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN) throws InvalidParamValueException, NotAuthorizedException {
        return accountAccessController.getBankAccountAccess(authToken, iBAN);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = InvalidPINException.class, code = 421)
    })
    public Object unblockCard(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("pinCard") String pinCard) throws NotAuthorizedException, InvalidParamValueException, NoEffectException, AccountFrozenException {
        cardController.unblockCard(authToken, iBAN, pinCard);
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = NoEffectException.class, code = 420)
    })
    public Object simulateTime(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("nrOfDays") int nrOfDays) throws InvalidParamValueException, NoEffectException, NotAuthorizedException {
        timeController.simulateTime(authToken, nrOfDays);
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = NoEffectException.class, code = 420),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object reset(@JsonRpcParam("authToken") String authToken) throws NoEffectException, NotAuthorizedException {
        timeController.reset(authToken);
        return new EmptyJsonResponse();
    }

    public Object getDate() {
        return timeController.getDate();
    }

    /**
     * Overdraft extension
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = NotAllowedException.class, code = 424)
    })
    public Object setOverdraftLimit(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("overdraftLimit") double overdraftLimit) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException, NotAllowedException {
        accountController.setOverdraftLimit(authToken, iBAN, overdraftLimit);
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object getOverdraftLimit(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN) throws NotAuthorizedException, InvalidParamValueException {
        return accountController.getOverdraftLimit(authToken, iBAN);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object getEventLogs(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("beginDate") Date beginDate, @JsonRpcParam("endDate") Date endDate) throws NotAuthorizedException {
        return loggingController.getEventLogs(authToken, beginDate, endDate);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = NotAllowedException.class, code = 424)
    })
    public Object openSavingsAccount(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException, NotAllowedException {
        accountSavingController.openSavingsAccount(authToken, iBAN);
        return new EmptyJsonResponse();
    }

    /**
     * Card invalidation extension
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object invalidateCard(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("pinCard") String pinCard, @JsonRpcParam("newPin") boolean newPin) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException {
        return cardController.invalidateCard(authToken, iBAN, pinCard, newPin);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = NotAllowedException.class, code = 424)
    })
    public Object requestCreditCard(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException, NotAllowedException {
        return creditCardController.requestCreditCard(authToken, iBAN);
    }

    /**
     * Administrive (II)
     */

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = NoEffectException.class, code = 420)
    })
    public Object setFreezeUserAccount(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("username") String username, @JsonRpcParam("freeze") boolean freeze) throws NotAuthorizedException, InvalidParamValueException, NoEffectException {
        accountController.setFreezeUserAccount(authToken, username, freeze);
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = NoEffectException.class, code = 420),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423)
    })
    public Object transferBankAccount(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("username") String username) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException, NoEffectException {
        accountAccessController.transferBankAccount(authToken, iBAN, username);
        return new EmptyJsonResponse();
    }

    /**
     * Spending limits
     */

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423)
    })
    public Object setTransferLimit(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("transferLimit") BigDecimal transferLimit) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException {
        accountController.setTransferLimit(authToken, iBAN, transferLimit);
        return new EmptyJsonResponse();
    }

    /**
     * Child bank account
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423)
    })
    public Object openAccount(@JsonRpcParam("name") String name,
                              @JsonRpcParam("surname") String surname,
                              @JsonRpcParam("initials") String initials,
                              @JsonRpcParam("dob") Date date,
                              @JsonRpcParam("ssn") String ssn,
                              @JsonRpcParam("address") String address,
                              @JsonRpcParam("telephoneNumber") String telephoneNumber,
                              @JsonRpcParam("email") String email,
                              @JsonRpcParam("username") String username,
                              @JsonRpcParam("password") String password,
                              @JsonRpcParam("type") String type,
                              @JsonRpcParam("guardians") String[] guardians) throws InvalidParamValueException, NotAuthorizedException, AccountFrozenException {
        return accountController.openAccount(name, surname, initials, date, ssn, address, telephoneNumber, email, username, password, type, guardians);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object setValue(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("key") String key, @JsonRpcParam("value") BigDecimal value, @JsonRpcParam("date") Date date) throws NotAuthorizedException, InvalidParamValueException {
        systemVariableController.setValue(authToken, key, value, date);
        return new EmptyJsonResponse();
    }

}
