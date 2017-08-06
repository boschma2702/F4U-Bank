package com.bank.repository.transaction;

import com.bank.bean.transaction.TransactionBean;
import com.bank.projection.transaction.TransactionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionBean, Integer> {
    @Query("select t " +
            "from TransactionBean t " +
            "where " +
            "t in (select t1 from TransactionBean t1 where t1.sourceBean.accountId = ?1 or t.targetBean.accountId = ?1) " +
            "or " +
            "t in (select t2 from TransactionBean t2 where t2.creditCardBean.accountBean.accountId = ?1) " +
            "order by date desc")
    Page<TransactionBean> getListOfXLatestTransactions(Pageable pageable, int accountId);

    List<TransactionBean> findTransactionBeansByDateAfter(Date date);

    @Query("select sum(t.amount) " +
            "from TransactionBean t " +
            "where t.fromSavings = false " +
            "and t.sourceBean.accountId = ?1 " +
            "and t.date > ?2 " +
            "and t.targetBean.accountId <> ?1")
    BigDecimal getTransferredAmountSince(int accountId, Date date);
}
