package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.AddressRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.service.AddressService;
import com.rab3tech.customer.service.LoginService;
import com.rab3tech.dao.entity.Address;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.AddressVO;
import com.rab3tech.vo.LoginVO;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private LoginRepository loginRepository;

	@Override
	public String updateAddress(AddressVO addressVO) {
		Optional<Address> oldAddress = addressRepository.findById(addressVO.getId());
		Login login = loginRepository.findByLoginid(addressVO.getLoginid()).get();
		if(!oldAddress.isPresent()) {
			Address address = new Address();
			BeanUtils.copyProperties(addressVO, address, Utils.ignoreNullData(addressVO));
			address.setLogin(login);
			address.setDoe(new Timestamp(new Date().getTime()));
			address.setDom(new Timestamp(new Date().getTime()));
			addressRepository.save(address);
		}else {
		BeanUtils.copyProperties(addressVO, oldAddress.get(), Utils.ignoreNullData(addressVO));
		oldAddress.get().setDom(new Timestamp(new Date().getTime()));
		addressRepository.save(oldAddress.get());
		}
		return "Successfully saved";
	}

	@Override
	public AddressVO findByLoginId(String string) {
		Login login = loginRepository.findByLoginid(string).get();
		Optional<Address> optional = addressRepository.findByLogin(login);
		AddressVO addressVO = new AddressVO();
		if (optional.isPresent()) {
			BeanUtils.copyProperties(optional.get(), addressVO);
		}
		return addressVO;
	}

	@Override
	public String deleteByLoginId(String username) {
		Login login = loginRepository.findByLoginid(username).get();
		Optional<Address> optional = addressRepository.findByLogin(login);
		if (optional.isPresent()) {
			addressRepository.delete(optional.get());
			return "Successfully deleted";
		}
		return "Sorry, You dont have address in the database, Please add your address to move further";
	}

	

}
