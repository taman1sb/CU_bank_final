package com.rab3tech.customer.dao.repository;

import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.dao.entity.TransactionEntity;



public interface TransactionRepository  extends JpaRepository<TransactionEntity, Integer> {


	List<TransactionEntity> findAllByDebitAccountNumber(String accountNumber);


	List<TransactionEntity> findAllByPayeeId(PayeeInfo payeeInfo);

	@Query("select t from TransactionEntity t where t.debitAccountNumber= :accountNumber or t.payeeId.payeeAccountNo=:accountNumber order by t.DOE desc ")
	List<TransactionEntity> getAllTransactions(@Param("accountNumber") String accountNumber);

}
