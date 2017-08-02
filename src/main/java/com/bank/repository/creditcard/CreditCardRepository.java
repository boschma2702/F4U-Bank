package com.bank.repository.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends CrudRepository<CreditCardBean, Integer> {

    @Query("select case when (count(c) > 0)  then true else false end " +
            "from CreditCardBean c " +
            "where c.creditCardNumber = ?1")
    boolean isCreditCardNumberTaken(String creditCardNumber);
}
