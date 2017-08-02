package com.bank.repository.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

@Repository
public interface CreditCardRepository extends CrudRepository<CreditCardBean, Integer> {

    @Query("select case when (count(c) > 0)  then true else false end " +
            "from CreditCardBean c " +
            "where c.creditCardNumber = ?1")
    boolean isCreditCardNumberTaken(String creditCardNumber);

    @Modifying
    @Query("update CreditCardBean c " +
            "set c.credit = c.credit + ?2 " +
            "where c.creditCardId = ?1")
    void updateAmount(int creditCardId, BigDecimal amount);

    @Query("Select c " +
            "from CreditCardBean c " +
            "where c.isActive = true " +
            "and c.creditCardNumber = ?1 " +
            "and c.activationDate < ?2")
    CreditCardBean findCreditCardBeanByCreditCardNumber(String creditCardNumber, Date currentDate);

    @Query("select case when (count(c) > 0)  then true else false end " +
            "from CreditCardBean c " +
            "where c.accountBean.accountId = ?1 " +
            "and c.isActive = true")
    boolean hasAccountIdCreditCard(int accountId);
}
