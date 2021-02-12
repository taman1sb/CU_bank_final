package com.rab3tech.customer.service;

import com.rab3tech.vo.AddressVO;
import com.rab3tech.vo.LoginVO;

public interface AddressService  {

	AddressVO findByLoginId(String string);

	String updateAddress(AddressVO addressVO);

	String deleteByLoginId(String username);


	

}
