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
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object openAdditionalAccount(@JsonRpcParam("authToken") String authToken) throws NotAuthorizedException {
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

    /**
     * Access module
     */

    @Autowired
    private AccountAccessController accountAccessController;

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NoEffectException.class, code = 420)
    })
    public Object provideAccess(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("username") String username) throws NotAuthorizedException, InvalidParamValueException, NoEffectException, AccountFrozenException {
        return accountAccessController.provideAccess(authToken, iBAN, username);
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NoEffectException.class, code = 420)
    })
    public Object revokeAccess(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("username") String username) throws NotAuthorizedException, InvalidParamValueException, NoEffectException, AccountFrozenException {
        accountAccessController.revokeAccess(authToken, iBAN, username);
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NoEffectException.class, code = 420)
    })
    public Object revokeAccess(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN) throws NotAuthorizedException, InvalidParamValueException, NoEffectException, AccountFrozenException {
        accountAccessController.revokeAccess(authToken, iBAN, null);
        return new EmptyJsonResponse();
    }

    /**
     * Transfer module
     */
    @Autowired
    private TransactionController transactionController;

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
    public Object payFromAccount(@JsonRpcParam("sourceIBAN") String sourceIBAN, @JsonRpcParam("targetIBAN") String targetIBAN, @JsonRpcParam("pinCard") String pinCard, @JsonRpcParam("pinCode") String pinCode, @JsonRpcParam("amount") double amount) throws InvalidParamValueException, InvalidPINException, AccountFrozenException {
        transactionController.payFromAccount(sourceIBAN, targetIBAN, pinCard, pinCode, new BigDecimal(amount));
        return new EmptyJsonResponse();
    }

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object transferMoney(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("sourceIBAN") String sourceIBAN, @JsonRpcParam("targetIBAN") String targetIBAN, @JsonRpcParam("targetName") String targetName, @JsonRpcParam("amount") double amount, @JsonRpcParam("description") String description) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException {
        transactionController.transferMoney(authToken, sourceIBAN, targetIBAN, targetName, new BigDecimal(amount), description);
        return new EmptyJsonResponse();
    }

    /**
     * Authentication module
     */
    @Autowired
    private SessionController sessionController;

    @JsonRpcErrors({
            @JsonRpcError(exception = AuthenticationException.class, code = 422)
    })
    public Object getAuthToken(@JsonRpcParam("username") String username, @JsonRpcParam("password") String password) throws AuthenticationException {
        return sessionController.getAuthToken(username, password);
    }

    /**
     * Info Module
     */
    @Autowired
    private CustomerController customerController;

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

    /**
     * Extension 2
     */
    @Autowired
    private CardController cardController;

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

    /**
     * Time extension
     */
    @Autowired
    private TimeController timeController;

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

    public Object getDate(){
        return timeController.getDate();
    }

    /**
     * Overdraft extension
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object setOverdraftLimit(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("overdraftLimit") double overdraftLimit) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException {
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

    /**
     * Logger extension
     */

    @Autowired
    private LoggingController loggingController;

    @JsonRpcErrors({
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object getEventLogs(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("beginDate") Date beginDate, @JsonRpcParam("endDate") Date endDate) throws NotAuthorizedException {
        return loggingController.getEventLogs(authToken, beginDate, endDate);
    }

    /**
     * Savings extension
     */

    @Autowired
    private AccountSavingController accountSavingController;

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object openSavingsAccount(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException {
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

    /**
     * CreditCard extension
     */

    @Autowired
    private CreditCardController creditCardController;

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParamValueException.class, code = 418),
            @JsonRpcError(exception = AccountFrozenException.class, code = 423),
            @JsonRpcError(exception = NotAuthorizedException.class, code = 419)
    })
    public Object requestCreditCard(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN) throws NotAuthorizedException, InvalidParamValueException, AccountFrozenException {
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
    public Object setFreezeUserAccount(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("freeze") boolean freeze) throws NotAuthorizedException, InvalidParamValueException, NoEffectException {
        accountController.setFreezeUserAccount(authToken, iBAN, freeze);
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
                              @JsonRpcParam("password") String password,
                              @JsonRpcParam("type") String type,
                              @JsonRpcParam("guardians") String[] guardians) throws InvalidParamValueException, NotAuthorizedException {
        return accountController.openAccount(name, surname, initials, date, ssn, address, telephoneNumber, email, username, password, type, guardians);
    }

}
