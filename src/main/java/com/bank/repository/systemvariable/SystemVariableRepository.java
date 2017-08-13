package com.bank.repository.systemvariable;

import com.bank.bean.systemvariables.SystemVariableBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SystemVariableRepository extends CrudRepository<SystemVariableBean, Integer> {

    @Query("select s " +
            "from SystemVariableBean s ")
    SystemVariableBean getSystemVariableBean();
}
