package com.bank.repository.card;

import com.bank.bean.card.CardBean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository
@Transactional
public interface CardRepository extends CrudRepository<CardBean, Integer> {
    @Modifying
    @Query("update CardBean c set c.isActive = false where c.accountBean.accountId = ?1")
    void invalidatePinCards(int accountId);

    @Modifying
    @Query("update CardBean c " +
            "set c.isActive = false " +
            "where c.isActive = true " +
            "and c.accountBean.accountId = ?1 " +
            "and c.pinCard = ?2 " +
            "and ?3 < c.experationDate")
    void invalidatePinCard(int accountId, String pinCard, Date currentDate);


    @Query("select c from CardBean c " +
            "where c.isActive = true " +
            "and c.accountBean.accountId = ?1 " +
            "and c.pinCard = ?2 " +
            "and ?3 < c.experationDate")
    CardBean getCardBean(int accountId, String pinCard, Date currentDate);


    @Query("select c " +
            "from CardBean c, AccountBean a " +
            "where c.isActive = false " +
            "and a.isActive = true " +
            "and c.accountBean.accountId = ?1 " +
            "and a.accountId = ?1 " +
            "and c.pinCard = ?2")
    CardBean getBlockedCardOfNonBlockedAccount(int accountId, String pinCard);

    void deleteCardBeansByCreationDateAfter(Date date);

    @Query("select case when (count(c) > 0) then true else false end " +
            "from CardBean c " +
            "where c.pinCard = ?1 " +
            "and c.accountBean.accountId = ?2")
    boolean isPinCardTaken(String pinCard, int accountId);
}
