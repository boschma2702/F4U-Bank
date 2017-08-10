package com.bank.repository.account;

import com.bank.bean.account.AccountBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.projection.customer.CustomerUsernameProjection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface AccountRepository extends CrudRepository<AccountBean, Integer> {
    AccountBean findAccountBeanByAccountNumber(String accountNumber);

    AccountBean findAccountBeansByAccountId(int accountId);

    @Modifying
    @Query("update AccountBean a set a.isActive = false where a.accountNumber = ?1")
    void closeAccount(String iBAN);

    @Modifying
    @Query("update AccountBean a " +
            "set a.amount = a.amount + ?2, " +
            "a.minimumDayAmount = case when (a.amount < a.minimumDayAmount) then a.amount else a.minimumDayAmount end " +
            "where a.accountId = ?1")
    void updateAmount(int accountId, BigDecimal amount);

    @Query("select new com.bank.projection.customer.CustomerUsernameProjection(person.username) " +
            "from CustomerAccount customeraccount, CustomerBean customer, PersonBean person " +
            "where customeraccount.accountId = ?1 " +
            "and customeraccount.customerId = customer.customerId " +
            "and person.customerBean.customerId = customer.customerId")
    List<CustomerUsernameProjection> getBankAccountAccess(int accountId);

    @Query("select a " +
            "from AccountBean a " +
            "where a.minimumDayAmount < 0 " +
            "and a.isActive = true")
    List<AccountBean> getActiveAccountBeansWithNegativeDayOverdraft();

    @Query("select a " +
            "from AccountBean a " +
            "where a.buildUpOverdraftInterest < 0 " +
            "and a.isActive = true")
    List<AccountBean> getActiveAccountBeansWithOverdraftInterest();

    @Modifying
    @Query("update AccountBean  a " +
            "set a.minimumDayAmount = a.amount " +
            "where a.isActive = true")
    void resetMinimumDayAmount();

    @Modifying
    @Query("update AccountBean a " +
            "set a.buildUpOverdraftInterest = 0 " +
            "where a.isActive = true")
    void resetBuildUpOverdraftInterest();

    @Query("select case when (count(a) > 0)  then true else false end " +
            "from AccountBean a " +
            "where a.accountNumber = ?1")
    boolean isAccountNumberTaken(String accountNumber);

    @Query("select a " +
            "from AccountBean a " +
            "where a.isActive = true " +
            "and a.isMinorAccount = true " +
            "and a.frozen = false ")
    List<AccountBean> getAllActiveMinorAccounts();

    @Modifying
    @Query("update AccountBean a " +
            "set a.buildUpInterest = 0 " +
            "where a.isActive = true")
    void resetBuildUpInterest();

}