package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.Role;
import com.ifcdpp.ifcdpp.entity.User;
import com.ifcdpp.ifcdpp.models.RegistrationModel;
import com.ifcdpp.ifcdpp.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public User getUser(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean registerUser(RegistrationModel registrationModel) {
        if (!registrationModel.getPassword().equals(registrationModel.getConfirm())) {
            return false;
        }
        if (getUser(registrationModel.getEmail()) != null) {
            return false;
        }

        User user = new User();

        user.setEmail(registrationModel.getEmail());
        user.setLogin(registrationModel.getLogin());
        user.setPassword(registrationModel.getPassword());
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return true;
    }

}
