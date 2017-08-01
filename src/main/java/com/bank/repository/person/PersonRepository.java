package com.bank.repository.person;

import com.bank.bean.person.PersonBean;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<PersonBean, Integer> {

    PersonBean findPersonBeanByUsername(String username);

}
