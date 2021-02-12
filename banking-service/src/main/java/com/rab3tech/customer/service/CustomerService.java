package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.dao.entity.SecurityQuestions;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.LoginVO;

public interface CustomerService {

	CustomerVO createAccount(CustomerVO customerVO);

	CustomerAccountInfoVO createBankAccount(int csaid);

	CustomerVO findProfileByEmail(String email);

	void updateProfile(CustomerVO customerVO);

	List<SecurityQuestions> findAllQuestion();

	String findAccountByEmail(String string);

	List<CustomerAccountInfoVO> findAllAccountByUserid(String username);

	

}
