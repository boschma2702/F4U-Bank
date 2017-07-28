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

    @Query("select new com.bank.projection.customer.CustomerUsernameProjection(customer.username) " +
            "from CustomerAccount customeraccount, CustomerBean customer " +
            "where customeraccount.accountId = ?1 " +
            "and customeraccount.customerId = customer.customerId")
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

}