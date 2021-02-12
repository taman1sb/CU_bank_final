package com.rab3tech.customer.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.vo.ApplicationResponseVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v3")
public class CustomerController {
	@Autowired
	private PayeeInfoService payeeInfoService;

	@GetMapping("/customer/deletePayee/{benId}")
	private ApplicationResponseVO deletePayee(@PathVariable int benId) {
		ApplicationResponseVO responseVO = new ApplicationResponseVO();
		try {
			payeeInfoService.deletePayee(benId);
			responseVO.setCode(200);
			responseVO.setMessage("Payee has been deleted");
			// responseVO.setStatus("Success");

		} catch (Exception e) {
			responseVO.setCode(404);
			responseVO.setMessage("Some error has occured while deleting");
			// responseVO.setStatus("Error");
		}
		return responseVO;

	}

	@PostMapping("/customer/updatePayee")
	private ApplicationResponseVO updatePayee(@RequestBody PayeeInfoVO payeeInfoVO) {
		ApplicationResponseVO responseVO = new ApplicationResponseVO();
		try {
			String str = payeeInfoService.updatePayee(payeeInfoVO);
				responseVO.setCode(200);
				responseVO.setMessage(str);
				// responseVO.setStatus("Success");

			} catch (Exception e) {
				responseVO.setCode(404);
				responseVO.setMessage("Some error has occured while update");
				// responseVO.setStatus("Error");
			}
			return responseVO;
	}

}
