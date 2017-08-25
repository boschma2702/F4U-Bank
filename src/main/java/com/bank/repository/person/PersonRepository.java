package com.bank.repository.person;

import com.bank.bean.person.PersonBean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<PersonBean, Integer> {

    PersonBean findPersonBeanByUsername(String username);

    @Modifying
    @Query("delete PersonBean p " +
            "where p.customerBean.customerId = ?1 ")
    void deletePersonByCustomerId(int customerId);

}
