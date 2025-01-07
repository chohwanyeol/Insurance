package com.insurance.insurance.service;

import com.insurance.insurance.dto.SignUpDTO;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.UserInfo;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public SiteUser createByDTO(SignUpDTO signUpDTO, UserInfo userInfo) {
        String username = signUpDTO.getUsername();
        String password = signUpDTO.getPassword();
        return create(username,password, userInfo);
    }

    @Transactional
    public SiteUser create(String username, String password, UserInfo userInfo){
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserInfo(userInfo);
        return userRepository.save(user);
    }


    public SiteUser getByUsername(String username) {
        SiteUser siteUser = userRepository.findByUsername(username).orElseThrow(()->new DataNotFoundException("해당 유저가 존재하지않음 : "+username ));
        return siteUser;
    }
}
