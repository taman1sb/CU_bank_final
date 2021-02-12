package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.PayeeInfoVO;

@Service
@Transactional

public class PayeeInfoServiceImp implements PayeeInfoService {

	@Autowired
	private MagicCustomerRepository customerRepository;

	@Autowired
	private PayeeInfoRepository payeeInfoRepository;

	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;

	@Override
	public String savePayee(PayeeInfoVO payeeInfoVO) {
		Optional<PayeeInfo> payeeInfo = payeeInfoRepository.findByPayeeNickName(payeeInfoVO.getPayeeNickName());
		if (payeeInfo.isPresent()) {
			return "Nick-name should be unique ";
		}
		Optional<CustomerAccountInfo> payeeAcount = customerAccountInfoRepository
				.findByAccountNumber(payeeInfoVO.getPayeeAccountNo());
		if (!payeeAcount.isPresent()) {
			return "Payee's account number is invalid ";
		}
		if (payeeAcount.get().getAccountType().getId() != 1) {
			return "Payee account is not saving account";
		}
		Customer customer = customerRepository.findByEmail(payeeInfoVO.getCustomerId()).get();
		Optional<CustomerAccountInfo> optional = customerAccountInfoRepository.findByCustomerId(customer.getLogin());
		if (payeeInfoVO.getPayeeAccountNo().equalsIgnoreCase(optional.get().getAccountNumber())) {
			return "Both parties should not have same account number";
		}
		Optional<PayeeInfo> payeeInfo2 = payeeInfoRepository.findByPayeeAccountNo(payeeInfoVO.getPayeeAccountNo());
		if (payeeInfo2.isPresent()) {
			return "Same payee canot be added again";
		}
		PayeeInfo entity = new PayeeInfo();
		BeanUtils.copyProperties(payeeInfoVO, entity);
		entity.setStatus("1");
		entity.setDoe(new Timestamp(new Date().getTime()));
		entity.setDom(new Timestamp(new Date().getTime()));
		payeeInfoRepository.save(entity);
		return "Payee has been added successfully";

	}

	@Override
	public List<PayeeInfoVO> getAllPayee(String username) {
		List<PayeeInfo> entity = payeeInfoRepository.findAllByCustomerId(username);
		List<PayeeInfoVO> payees = new ArrayList<PayeeInfoVO>();
		for (PayeeInfo payeeInfo : entity) {
			PayeeInfoVO vo = new PayeeInfoVO();
			BeanUtils.copyProperties(payeeInfo, vo);
			payees.add(vo);
		}
		return payees;
	}

	@Override
	public void deletePayee(int id) throws Exception {
		payeeInfoRepository.deleteById(id);
	}

	@Override
	public String updatePayee(PayeeInfoVO payeeInfoVO) throws Exception {
		/*
		 * Optional<PayeeInfo> payeeInfo =
		 * payeeInfoRepository.findByPayeeNickName(payeeInfoVO.getPayeeNickName());
		 * 
		 * if (payeeInfo.isPresent()) { return "Nick-name should be unique "; }
		 */
		Optional<CustomerAccountInfo> payeeAcount = customerAccountInfoRepository
				.findByAccountNumber(payeeInfoVO.getPayeeAccountNo());
		if (!payeeAcount.isPresent()) {
			return "Payee's account number is invalid ";
		}
		if (payeeAcount.get().getAccountType().getId() != 1) {
			return "Payee account is not saving account";
		}
		/*
		 * Customer customer =
		 * customerRepository.findByEmail(payeeInfoVO.getCustomerId()).get();
		 * Optional<CustomerAccountInfo> optional =
		 * customerAccountInfoRepository.findByCustomerId(customer.getLogin()); if
		 * (payeeInfoVO.getPayeeAccountNo().equalsIgnoreCase(optional.get().
		 * getAccountNumber())) { return
		 * "Both parties should not have same account number"; }
		 */
		/*
		 * Optional<PayeeInfo> payeeInfo2 =
		 * payeeInfoRepository.findByPayeeAccountNo(payeeInfoVO.getPayeeAccountNo()); if
		 * (payeeInfo2.isPresent()) { return "Same payee canot be added again"; }
		 */
		PayeeInfo entity = payeeInfoRepository.findById(payeeInfoVO.getId()).get();
		String customerID= entity.getCustomerId();
		BeanUtils.copyProperties(payeeInfoVO, entity,Utils.ignoreNullData(payeeInfoVO));
		entity.setCustomerId(customerID);
		entity.setDoe(new Timestamp(new Date().getTime()));
		entity.setDom(new Timestamp(new Date().getTime()));
		payeeInfoRepository.save(entity);
		return "Payee has been updated successfully";

	}

}
