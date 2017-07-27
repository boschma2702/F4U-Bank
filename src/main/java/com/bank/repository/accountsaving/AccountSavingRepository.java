package com.bank.repository.accountsaving;

import com.bank.bean.account.AccountBean;
import com.bank.bean.acountsavings.AccountSavingBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccountSavingRepository extends CrudRepository<AccountSavingBean, Integer> {

    AccountSavingBean findAccountSavingBeanByAccountBean(AccountBean accountBean);

}
