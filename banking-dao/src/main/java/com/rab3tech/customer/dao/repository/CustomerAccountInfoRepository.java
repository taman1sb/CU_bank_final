package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.Login;




public interface CustomerAccountInfoRepository extends JpaRepository<CustomerAccountInfo, Long> {

	Optional<CustomerAccountInfo> findByCustomerId(Login login);

	Optional<CustomerAccountInfo> findByAccountNumber(String payeeAccountNo);

	List<CustomerAccountInfo> findAllByCustomerId(Login login);

}
