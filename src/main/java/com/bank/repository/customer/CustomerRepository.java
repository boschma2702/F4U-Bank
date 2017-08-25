package com.bank.repository.customer;

import com.bank.bean.account.AccountBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.projection.customer.CustomerAccountAccessProjection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerBean, Integer> {
    CustomerBean getCustomerBeanByCustomerId(int customerId);

//    CustomerBean getCustomerBeanByUsername(String username);

    @Modifying
    @Query("update CustomerBean c set c.isActive = false where c.customerId = ?1")
    void closeCustomer(int customerId);

    @Query("select new com.bank.projection.customer.CustomerAccountAccessProjection(account.accountNumber, person.username) " +
            "from CustomerBean customer, CustomerAccount customeraccount, CustomerAccount main, AccountBean account, PersonBean person " +
            "where customeraccount.customerId = ?1 " +
            "and main.isMain = true " +
            "and customeraccount.accountId = main.accountId " +
            "and customeraccount.accountId = account.accountId " +
            "and main.customerId = customer.customerId " +
            "and person.customerBean.customerId = customer.customerId")
    List<CustomerAccountAccessProjection> getCustomerAccess(int customerId);

    @Query("select account " +
            "from CustomerBean c, CustomerAccount customeraccount, AccountBean account " +
            "where c.customerId = ?1 " +
            "and c.customerId = customeraccount.customerId " +
            "and customeraccount.isMain = true " +
            "and account.accountId = customeraccount.accountId " +
            "and account.isActive = true")
    List<AccountBean> getCustomerBeanMainAccess(int customerId);

    void deleteCustomerBeansByCreationDateAfter(Date date);

    @Query("select c " +
            "from CustomerBean c " +
            "where day(c.dob) = day(?1) " +
            "and month(c.dob) = month(?1) " +
            "and year(?1) - year(dob) = ?2")
    List<CustomerBean> getMinorBirthdays(Date date, int minorAge);

    @Query("select case when (count(c) > 0)  then true else false end " +
            "from CustomerBean c " +
            "where c.customerId = ?1 " +
            "and c.frozen = true")
    boolean isCustomerFrozen(int customerId);

}
