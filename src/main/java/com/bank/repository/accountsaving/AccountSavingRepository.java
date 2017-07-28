package com.bank.repository.accountsaving;

import com.bank.bean.account.AccountBean;
import com.bank.bean.acountsavings.AccountSavingBean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface AccountSavingRepository extends CrudRepository<AccountSavingBean, Integer> {

    AccountSavingBean findAccountSavingBeanByAccountBean(AccountBean accountBean);

    @Modifying
    @Query("update AccountSavingBean a " +
            "set a.amount = a.amount + ?2, " +
            "a.minimumDayAmount = case when (a.amount < a.minimumDayAmount) then a.amount else a.minimumDayAmount end " +
            "where a.accountBean.accountId = ?1")
    void updateAmount(int accountId, BigDecimal amount);

    @Modifying
    @Transactional
    @Query("update AccountSavingBean  a " +
            "set a.minimumDayAmount = a.amount")
    void resetMinimumDayAmount();

    @Modifying
    @Transactional
    @Query("update AccountSavingBean  a " +
            "set a.buildUpInterest = 0")
    void resetBuildUpInterest();
}
