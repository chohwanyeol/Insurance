package com.insurance.insurance;

import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.UserInfo;
import com.insurance.insurance.repository.UserInfoRepository;
import com.insurance.insurance.repository.UserRepository;
import com.insurance.insurance.service.UserInfoService;
import com.insurance.insurance.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootTest
class InsuranceApplicationTests {
	@Autowired
	private UserService userService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserInfoRepository userInfoRepository;
	@Test
	void createAccount() {

		UserInfo userInfo = userInfoRepository.findById(1).orElseThrow();

		String username = "admin1234";
		String password = passwordEncoder.encode("admin1234");
		SiteUser siteUser = userService.create(username,password,userInfo);
	}

}
