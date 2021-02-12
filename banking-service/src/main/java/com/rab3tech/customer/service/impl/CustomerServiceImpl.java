package com.rab3tech.customer.service.impl;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.AccountStatusRepository;

import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountApprovedRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountEnquiryRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerQuestionsAnsRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.RoleRepository;
import com.rab3tech.customer.dao.repository.SecurityQuestionsRepository;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.dao.entity.AccountStatus;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.dao.entity.CustomerSavingApproved;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.Role;
import com.rab3tech.dao.entity.SecurityQuestions;
import com.rab3tech.utils.AccountStatusEnum;
import com.rab3tech.utils.PasswordGenerator;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.AccountTypeVO;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.LoginVO;

import ch.qos.logback.core.joran.action.Action;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private CustomerQuestionsAnsRepository customerQARepository;

	@Autowired
	private MagicCustomerRepository customerRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AccountStatusRepository accountStatusRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private CustomerAccountEnquiryRepository customerAccountEnquiryRepository;

	@Autowired
	private CustomerAccountApprovedRepository customerAccountApprovedRepository;

	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;

	@Autowired
	private CustomerQuestionsAnsRepository cQAns;

	@Autowired
	private SecurityQuestionService securityQuestionService;

	@Override
	public CustomerAccountInfoVO createBankAccount(int csaid) {
		// logic
		String customerAccount = Utils.generateCustomerAccount();
		CustomerSaving customerSaving = customerAccountEnquiryRepository.findById(csaid).get();

		CustomerAccountInfo customerAccountInfo = new CustomerAccountInfo();
		customerAccountInfo.setAccountNumber(customerAccount);
		customerAccountInfo.setAccountType(customerSaving.getAccType());
		customerAccountInfo.setAvBalance(1000.0F);
		customerAccountInfo.setBranch(customerSaving.getLocation());
		customerAccountInfo.setCurrency("$");
		Customer customer = customerRepository.findByEmail(customerSaving.getEmail()).get();
		customerAccountInfo.setCustomerId(customer.getLogin());
		customerAccountInfo.setStatusAsOf(new Date());
		customerAccountInfo.setTavBalance(1000.0F);
		CustomerAccountInfo customerAccountInfo2 = customerAccountInfoRepository.save(customerAccountInfo);

		CustomerSavingApproved customerSavingApproved = new CustomerSavingApproved();
		BeanUtils.copyProperties(customerSaving, customerSavingApproved);
		customerSavingApproved.setAccType(customerSaving.getAccType());
		customerSavingApproved.setStatus(customerSaving.getStatus());
		// saving entity into customer_saving_enquiry_approved_tbl
		customerAccountApprovedRepository.save(customerSavingApproved);

		// delete data from
		customerAccountEnquiryRepository.delete(customerSaving);

		CustomerAccountInfoVO accountInfoVO = new CustomerAccountInfoVO();
		BeanUtils.copyProperties(customerAccountInfo2, accountInfoVO);
		return accountInfoVO;

	}

	@Override
	public CustomerVO createAccount(CustomerVO customerVO) {
		Customer pcustomer = new Customer();
		BeanUtils.copyProperties(customerVO, pcustomer);
		Login login = new Login();
		login.setNoOfAttempt(3);
		login.setLoginid(customerVO.getEmail());
		login.setName(customerVO.getName());
		String genPassword = PasswordGenerator.generateRandomPassword(8);
		customerVO.setPassword(genPassword);
		login.setPassword(bCryptPasswordEncoder.encode(genPassword));
		login.setToken(customerVO.getToken());
		login.setLocked("no");

		Role entity = roleRepository.findById(3).get();
		Set<Role> roles = new HashSet<>();
		roles.add(entity);
		// setting roles inside login
		login.setRoles(roles);
		// setting login inside
		pcustomer.setLogin(login);
		Customer dcustomer = customerRepository.save(pcustomer);
		customerVO.setId(dcustomer.getId());
		customerVO.setUserid(customerVO.getUserid());

		Optional<CustomerSaving> optional = customerAccountEnquiryRepository.findByEmail(dcustomer.getEmail());
		if (optional.isPresent()) {
			CustomerSaving customerSaving = optional.get();
			AccountStatus accountStatus = accountStatusRepository.findByCode(AccountStatusEnum.REGISTERED.getCode())
					.get();
			customerSaving.setStatus(accountStatus);
		}
		return customerVO;
	}

	@Autowired
	private SecurityQuestionsRepository securityQuestionsRepository;

	@Override
	public CustomerVO findProfileByEmail(String email) {

		Customer customer = customerRepository.findByEmail(email).get();

		// Logger.debug(customer.toString());
		CustomerVO customerVO = new CustomerVO();
		BeanUtils.copyProperties(customer, customerVO);
		System.out.println(customerVO.toString());
		java.util.List<CustomerQuestionAnswer> questionAnswer = cQAns.findQuestionAnswer(email);
		customerVO.setQuestion1(questionAnswer.get(0).getQuestion());
		customerVO.setQuestion2(questionAnswer.get(1).getQuestion());
		customerVO.setAnswer1(questionAnswer.get(0).getAnswer());
		customerVO.setAnswer2(questionAnswer.get(1).getAnswer());

		return customerVO;
	}

	@Override
	public List<SecurityQuestions> findAllQuestion() {
		List<SecurityQuestions> questionsList = securityQuestionsRepository.findAll();
		return questionsList;
	}

	@Override
	public void updateProfile(CustomerVO customerVO) {

		String methodName = "updateCustomer";
		logger.debug(methodName);
		logger.debug(customerVO.toString());
		Customer customer = customerRepository.findByEmail(customerVO.getEmail()).get();
		BeanUtils.copyProperties(customerVO, customer, Utils.ignoreNullData(customerVO));
		customerRepository.save(customer);
		List<CustomerQuestionAnswer> qAnswers = customerQARepository.findQuestionAnswer(customerVO.getEmail());
		if (qAnswers.get(0).getAnswer().equals(customerVO.getAnswer1())
				&& qAnswers.get(1).getAnswer().equals(customerVO.getAnswer2())) {
			logger.debug("No changes were made");
		} else {
			CustomerSecurityQueAnsVO questionAnswer = new CustomerSecurityQueAnsVO();
			questionAnswer.setLoginid(customerVO.getEmail());
			questionAnswer.setSecurityQuestion1(customerVO.getQuestion1());
			questionAnswer.setSecurityQuestion2(customerVO.getQuestion2());
			questionAnswer.setSecurityQuestionAnswer1(customerVO.getAnswer1());
			questionAnswer.setSecurityQuestionAnswer2(customerVO.getAnswer2());
			// CustomerQuestionAnswer questionAnswer2 = new CustomerQuestionAnswer
			securityQuestionService.save(questionAnswer);

		}
	}

	@Override
	public String findAccountByEmail(String username) {
		Customer customer = customerRepository.findByEmail(username).get();
		List<CustomerAccountInfo> listCustomerAccountInfo = customerAccountInfoRepository
				.findAllByCustomerId(customer.getLogin());
		for (CustomerAccountInfo each : listCustomerAccountInfo) {
			if (each.getAccountType().getId() == 1) {
				return null;
			}
		}

		if (listCustomerAccountInfo.isEmpty()) {
			return "You don't have account to add Payee ";

		} else {
			return "You don't have saving account!";
		}

	}

	@Override
	public List<CustomerAccountInfoVO> findAllAccountByUserid(String username) {
		Optional<Login> login = loginRepository.findById(username);
		List<CustomerAccountInfoVO> listVo = new ArrayList<CustomerAccountInfoVO>();
		if (login.isPresent()) {
			List<CustomerAccountInfo> entityList = customerAccountInfoRepository.findAllByCustomerId(login.get());
			for (CustomerAccountInfo each : entityList) {
				CustomerAccountInfoVO vo = new CustomerAccountInfoVO();
				BeanUtils.copyProperties(each, vo);
				listVo.add(vo);
			}
		}
		return listVo;
	}
}
