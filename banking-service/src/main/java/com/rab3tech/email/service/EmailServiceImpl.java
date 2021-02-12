package com.rab3tech.email.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.AddressRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.dao.entity.Address;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.vo.AddressVO;
import com.rab3tech.vo.EmailVO;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Override
	@Async("threadPool")
	public String sendRegistrationEmail(EmailVO mail) {

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Context context = new Context();
			Map<String, Object> props = new HashMap<>();
			props.put("name", mail.getName());
			props.put("sign", "By Cubic Bank");
			props.put("location", "Fremont CA100 , USA");
			props.put("email", "javahunk2020@gmail.com");
			props.put("registrationlink", mail.getRegistrationlink());
			context.setVariables(props);
			String html = templateEngine.process("send-registration-link", context);
			helper.setTo(mail.getTo());
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom(mail.getFrom());

			File cfile = new ClassPathResource("images/registration-banner.png",
					EmailServiceImpl.class.getClassLoader()).getFile();
			byte[] cbytes = Files.readAllBytes(cfile.toPath());
			InputStreamSource cimageSource = new ByteArrayResource(cbytes);
			helper.addInline("cb", cimageSource, "image/png");

			File file = new ClassPathResource("images/bank-icon.png", EmailServiceImpl.class.getClassLoader())
					.getFile();
			byte[] bytes = Files.readAllBytes(file.toPath());
			InputStreamSource imageSource = new ByteArrayResource(bytes);
			helper.addInline("bankIcon", imageSource, "image/png");

			javaMailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "done";
	}

	@Override
	@Async("threadPool")
	public String sendUsernamePasswordEmail(EmailVO mail) {

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Context context = new Context();
			Map<String, Object> props = new HashMap<>();
			props.put("name", mail.getName());
			props.put("username", mail.getUsername());
			props.put("password", mail.getPassword());
			props.put("sign", "Banking Application");
			props.put("location", "Fremont CA100 , USA");
			props.put("email", "javahunk2020@gmail.com");
			context.setVariables(props);
			String html = templateEngine.process("password-email-template", context);
			helper.setTo(mail.getTo());
			helper.setText(html, true);
			helper.setSubject("Regarding your banking username and password.");
			helper.setFrom(mail.getFrom());
			File cfile = new ClassPathResource("images/password.jpg", EmailServiceImpl.class.getClassLoader())
					.getFile();
			byte[] cbytes = Files.readAllBytes(cfile.toPath());
			InputStreamSource cimageSource = new ByteArrayResource(cbytes);
			helper.addInline("cb", cimageSource, "image/png");

			File file = new ClassPathResource("images/bank-icon.png", EmailServiceImpl.class.getClassLoader())
					.getFile();
			byte[] bytes = Files.readAllBytes(file.toPath());
			InputStreamSource imageSource = new ByteArrayResource(bytes);
			helper.addInline("bankIcon", imageSource, "image/png");

			javaMailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "done";
	}

	@Override
	@Async("threadPool")
	public String sendEquiryEmail(EmailVO mail) {

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Context context = new Context();
			Map<String, Object> props = new HashMap<>();
			props.put("name", mail.getName());
			props.put("sign", "Banking Application");
			props.put("location", "Fremont CA100 , USA");
			props.put("email", "javahunk2020@gmail.com");
			context.setVariables(props);
			String html = templateEngine.process("enquiry-email-template", context);
			helper.setTo(mail.getTo());
			helper.setText(html, true);
			helper.setSubject("Regarding Account enquiry to open an account.");
			helper.setFrom(mail.getFrom());

			File cfile = new ClassPathResource("images/cb1.png", EmailServiceImpl.class.getClassLoader()).getFile();
			byte[] cbytes = Files.readAllBytes(cfile.toPath());
			InputStreamSource cimageSource = new ByteArrayResource(cbytes);
			helper.addInline("cb", cimageSource, "image/png");

			File file = new ClassPathResource("images/bank-icon.png", EmailServiceImpl.class.getClassLoader())
					.getFile();
			byte[] bytes = Files.readAllBytes(file.toPath());
			InputStreamSource imageSource = new ByteArrayResource(bytes);
			helper.addInline("bankIcon", imageSource, "image/png");

			javaMailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "done";
	}

	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public String sendCheckEquiryEmail(String accountNumber) {
		Optional<CustomerAccountInfo> accountEntity = customerAccountInfoRepository.findByAccountNumber(accountNumber);
		if (accountEntity.isPresent()) {
			Optional<Address> address = addressRepository.findByLogin(accountEntity.get().getCustomerId());
			if (address.isPresent()) {

				AddressVO addressVO = new AddressVO();
				BeanUtils.copyProperties(address.get(), addressVO);

				EmailVO mail = new EmailVO();
				mail.setAddress(addressVO);
				mail.setName(addressVO.getFname() + " " + addressVO.getLname());
				mail.setSubject("Regarding Check Book request enquiry");
				mail.setFrom("rab3java2020@gmail.com");
				mail.setTo("rab3java2020@gmail.com");
				mail.setAccountNum(accountNumber);
				try {
					MimeMessage message = javaMailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message,
							MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
					Context context = new Context();
					Map<String, Object> props = new HashMap<>();
					props.put("name", mail.getName());
					props.put("sign", "Bank C.S");
					props.put("location", "XYZ 1234 , USA");
					props.put("email", mail.getTo());
					props.put("acc", mail.getAccountNum());
					props.put("addressVO", mail.getAddress());
					props.put("to", accountEntity.get().getCustomerId().getLoginid());
					context.setVariables(props);
					String html = templateEngine.process("checkbook-email-template.html", context);
					helper.setTo(mail.getTo());
					helper.setText(html, true);
					helper.setSubject(mail.getSubject());
					helper.setFrom(mail.getFrom());

					File cfile = new ClassPathResource("images/cb1.png", EmailServiceImpl.class.getClassLoader())
							.getFile();
					byte[] cbytes = Files.readAllBytes(cfile.toPath());
					InputStreamSource cimageSource = new ByteArrayResource(cbytes);
					helper.addInline("cb", cimageSource, "image/png");

					File file = new ClassPathResource("images/bank-icon.png", EmailServiceImpl.class.getClassLoader())
							.getFile();
					byte[] bytes = Files.readAllBytes(file.toPath());
					InputStreamSource imageSource = new ByteArrayResource(bytes);
					helper.addInline("bankIcon", imageSource, "image/png");

					javaMailSender.send(message);
					return "Your checkbook request has been sent to Admin for approval. Thank You!";

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} else
				return null;
		}
		return null;
	}

}
