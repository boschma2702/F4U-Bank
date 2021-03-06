package com.bank.repository.customeraccount;

import com.bank.bean.customeraccount.CustomerAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface CustomerAccountRepository extends CrudRepository<CustomerAccount, Integer> {
    @Query("select a from CustomerAccount a where a.customerId = ?1 and a.accountBean.isActive = true")
    List<CustomerAccount> getActiveCustomerAcounts(int customerId);

    CustomerAccount getFirstByAccountIdAndCustomerId(int accountId, int customerId);

    @Query("select c " +
            "from CustomerAccount c " +
            "where c.isMain = true " +
            "and c.accountId = ?1")
    CustomerAccount getMainCustomerAccount(int accountId);

    void deleteByCustomerIdAndAccountId(int customerId, int accountId);

    void deleteCustomerAccountsByCreationDateAfter(Date date);

    @Modifying
    @Query("delete from CustomerAccount c " +
            "where c.accountId = ?1 " +
            "and c.isMain = false")
    void removeNoneMainAccountHolders(int accountId);
}
