package com.insurance.insurance.service;

import com.insurance.insurance.dto.SignUpDTO;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser createUserByDTO(SignUpDTO signUpDTO) {
        String username = signUpDTO.getUsername();
        String password = signUpDTO.getPassword();
        String email = signUpDTO.getEmail();
        return create(username,password,email);
    }
    public SiteUser create(String username, String password, String email){
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
}
