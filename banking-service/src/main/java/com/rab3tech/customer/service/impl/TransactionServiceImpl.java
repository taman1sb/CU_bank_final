package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.dao.repository.TransactionRepository;
import com.rab3tech.customer.service.TransactionService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.dao.entity.TransactionEntity;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.StatementVO;
import com.rab3tech.vo.TransactionVO;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private PayeeInfoRepository payeeInfoRepository;

	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public String trasferCheck(TransactionVO transcationVo) {

		TransactionEntity entity = new TransactionEntity();

		Customer customer = customerRepository.findByEmail(transcationVo.getCustomerId()).get();
		Optional<CustomerAccountInfo> debitor = customerAccountInfoRepository.findByCustomerId(customer.getLogin());
		if (debitor.isPresent()) {
			transcationVo.setDebitAccountNumber(debitor.get().getAccountNumber());
			if (debitor.get().getAccountType().getId() != 1) {
				return "You don't have saving account!";
			}
			// no column to determine if the account is active or not
			float balance = debitor.get().getAvBalance();
			if (balance < transcationVo.getTransferAmount()) {
				return "Amount should not exceeds more than available balance of : " + debitor.get().getAvBalance();
			}
			debitor.get().setAvBalance((balance - transcationVo.getTransferAmount()));

		}

		PayeeInfo payeeInfo = payeeInfoRepository.findById(transcationVo.getPayeeID()).get();
		Optional<CustomerAccountInfo> creditor = customerAccountInfoRepository
				.findByAccountNumber(payeeInfo.getPayeeAccountNo());
		if (creditor.isPresent()) {
			if (creditor.get().getAccountType().getId() != 1) {
				return "Benefecery  don't have saving account!";
			}
			// no column to determine if the account is active or not
			float balance2 = creditor.get().getAvBalance();
			creditor.get().setAvBalance((balance2 + transcationVo.getTransferAmount()));
		}
		customerAccountInfoRepository.save(debitor.get());
		customerAccountInfoRepository.save(creditor.get());
		entity.setDebitAccountNumber(transcationVo.getDebitAccountNumber());
		entity.setDescription(transcationVo.getDescription());
		entity.setAmount(transcationVo.getTransferAmount());
		entity.setDOE(new Timestamp(new Date().getTime()));
		entity.setPayeeId(payeeInfo);
		transactionRepository.save(entity);
		return "Funds successfully transfered";
	}

	@Override
	public List<TransactionVO> getAlltransaction(String username) {

		Customer customer = customerRepository.findByEmail(username).get();
		Optional<CustomerAccountInfo> cusAcc = customerAccountInfoRepository.findByCustomerId(customer.getLogin());
		List<TransactionEntity> transactionEntityList = transactionRepository
				.getAllTransactions(cusAcc.get().getAccountNumber());
		List<TransactionVO> listStatement = new ArrayList<TransactionVO>();
		for (TransactionEntity each : transactionEntityList) {
			TransactionVO vo = new TransactionVO();
			vo.setTransferAmount(each.getAmount());
			vo.setDoe(each.getDOE());
			vo.setDescription(each.getDescription());
			if (each.getDebitAccountNumber().equals(cusAcc.get().getAccountNumber())) 
			{vo.setType("Debit");} else {vo.setType("Credit");}
			PayeeInfoVO payeevo = new PayeeInfoVO();
			BeanUtils.copyProperties(each.getPayeeId(), payeevo);
			vo.setPayeeName(payeevo.getPayeeName());
			listStatement.add(vo);
		}
		return listStatement;
	}

	/*
	 * @Override public List<StatementVO> getAlltransaction(String username) {
	 * Customer customer = customerRepository.findByEmail(username).get();
	 * Optional<CustomerAccountInfo> cusAcc =customerAccountInfoRepository.findByCustomerId(customer.getLogin());
	 * List<TransactionEntity> transactionEntityList =transactionRepository.findAllByDebitAccountNumber(cusAcc.get().getAccountNumber()); 
	 * Optional<PayeeInfo> payee =payeeInfoRepository.findByPayeeAccountNo(cusAcc.get().getAccountNumber());
	 * List<StatementVO> listStatement = new ArrayList<StatementVO>();
	 * if(payee.isPresent()) { 
	 * List<TransactionEntity> secondTransactionList = transactionRepository.findAllByPayeeId(payee.get());
	 * transactionEntityList.addAll(secondTransactionList);
	 * Collections.reverse(transactionEntityList); } 
	 * for (TransactionEntity each : transactionEntityList) {
	 * StatementVO vo = new StatementVO();
	 * vo.setAmount(each.getAmount()); vo.setDoe(each.getDOE());
	 * vo.setPayeeName(each.getPayeeId().getPayeeName()); 
	 * if(each.getDebitAccountNumber().equals(cusAcc.get().getAccountNumber())) 
	 * { vo.setType("Debit"); } else { vo.setType("Credit"); }
	 * vo.setRemarks(each.getDescription()); 
	 * listStatement.add(vo); } 
	 * return listStatement; }
	 */
	 
}
