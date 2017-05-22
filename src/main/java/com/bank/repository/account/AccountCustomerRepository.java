package com.bank.repository.account;

import com.bank.bean.customer.CustomerAccount;
import com.bank.bean.customer.CustomerAccountId;
import com.bank.projection.account.AccountCustomerDetailsProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountCustomerRepository extends CrudRepository<CustomerAccount, Integer> {

    List<AccountCustomerDetailsProjection> findCustomerAccountsByAccountId(int accountId);

}