package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.vo.PayeeInfoVO;

public interface PayeeInfoService {

	String savePayee(PayeeInfoVO payeeInfoVO) throws Exception;

	List<PayeeInfoVO> getAllPayee(String username);
	void deletePayee(int id) throws Exception;

	String updatePayee(PayeeInfoVO payeeInfoVO) throws Exception;

	



}
