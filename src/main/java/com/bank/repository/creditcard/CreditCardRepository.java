package com.bank.repository.creditcard;

import com.bank.bean.creditcard.CreditCardBean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
            "and c.activationDate < ?2 " +
            "and ?2 < c.expirationDate")
    CreditCardBean findActiveCreditCardBeanAfterActivationByCreditCardNumber(String creditCardNumber, Date currentDate);

    @Query("Select c " +
            "from CreditCardBean c " +
            "where c.isActive = true " +
            "and c.creditCardNumber = ?1 " +
            "and ?2 < c.expirationDate")
    CreditCardBean findActiveCreditCardBeanByCreditCardNumber(String creditCardNumber, Date currentDate);

    @Query("select c " +
            "from CreditCardBean c " +
            "where c.isActive = true " +
            "and c.accountBean.accountId = ?1 " +
            "and c.activationDate < ?2 " +
            "and ?2 < c.expirationDate")
    CreditCardBean getCreditCardBeanAfterActivationByAccountId(int accountId, Date currentDate);

    @Query("select c " +
            "from CreditCardBean c " +
            "where c.isActive = true " +
            "and c.accountBean.accountId = ?1 " +
            "and ?2 < c.expirationDate")
    CreditCardBean getCreditCardBeanByAccountId(int accountId, Date currentDate);

    @Query("select case when (count(c) > 0)  then true else false end " +
            "from CreditCardBean c " +
            "where c.accountBean.accountId = ?1 " +
            "and c.isActive = true " +
            "and ?2 < c.expirationDate")
    boolean hasAccountIdCreditCard(int accountId, Date currentDate);

    @Query("select c " +
            "from CreditCardBean c " +
            "where c.isActive = true " +
            "and c.credit <> c.creditLimit " +
            "and c.activationDate < ?1 " +
            "and ?1 < c.expirationDate")
    List<CreditCardBean> getCreditCardsWithUsedCredit(Date currentDate);

    @Query("select c " +
            "from CreditCardBean c " +
            "where c.isActive = true " +
            "and c.activationDate < ?1")
    List<CreditCardBean> getAllActiveCreditCards(Date currentDate);

    @Query("select c " +
            "from CreditCardBean c " +
            "where c.isActive = false " +
            "and c.creditCardNumber = ?1")
    CreditCardBean getBlockedCardByCreditCardNumber(String creditCardNumber);

    CreditCardBean findCreditCardBeanByCreditCardNumber(String creditCardNumber);
}
