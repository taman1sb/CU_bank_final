package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.vo.StatementVO;
import com.rab3tech.vo.TransactionVO;

public interface TransactionService {

	String trasferCheck(TransactionVO transcationVo);

	List<TransactionVO> getAlltransaction(String username);

}
