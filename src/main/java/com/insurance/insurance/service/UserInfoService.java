package com.insurance.insurance.service;

import com.insurance.insurance.dto.SignUpDTO;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.UserInfo;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.repository.UserInfoRepository;
import com.insurance.insurance.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;



@RequiredArgsConstructor
@Service
public class UserInfoService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public UserInfo createUserInfoByDTO(SignUpDTO signUpDTO) {
        String name = signUpDTO.getName();
        LocalDate birthday = signUpDTO.getBirthDay();
        String email = signUpDTO.getEmail();
        String location = signUpDTO.getLocation().trim() + " " + signUpDTO.getDetailLocation().trim();
        return create(name,birthday,email,location);
    }

    @Transactional
    public UserInfo create(String name, LocalDate birthday,String email, String location){
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setBirthDay(birthday);
        userInfo.setEmail(email);
        userInfo.setLocation(location);
        userInfo.setCreate_date(LocalDate.now());
        userInfo.setUpdate_date(LocalDate.now());
        return userInfoRepository.save(userInfo);
    }

}
