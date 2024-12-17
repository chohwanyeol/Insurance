package com.insurance.insurance.service;

import com.insurance.insurance.dto.SignUpDTO;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.UserInfo;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.repository.UserInfoRepository;
import com.insurance.insurance.repository.UserRepository;
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
    public UserInfo createUserInfoByDTO(SignUpDTO signUpDTO) {
        String username = signUpDTO.getUsername();
        String name = signUpDTO.getName();
        LocalDate birthday = signUpDTO.getBirthDay();
        return create(username,name,birthday);
    }

    public UserInfo create(String username, String name, LocalDate birthday){
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setBirthDay(birthday);
        userInfo.setSiteUser(userRepository.findByUsername(username).orElse(null));
        userInfo.setCreate_date(LocalDate.now());
        userInfo.setUpdate_date(LocalDate.now());
        return userInfoRepository.save(userInfo);
    }

    UserInfo getBySiteUser(SiteUser siteUser) {
        return userInfoRepository.findBySiteUser(siteUser).orElseThrow(
                ()->new DataNotFoundException("데이터가 존재하지 않습니다.")
        );
    }
}
