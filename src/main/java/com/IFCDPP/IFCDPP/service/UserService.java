package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.Role;
import com.ifcdpp.ifcdpp.entity.User;
import com.ifcdpp.ifcdpp.exceptions.MessageException;
import com.ifcdpp.ifcdpp.models.Profile;
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

    public Profile getUserProfile(Long userId) {
        User entity = userRepository.findById(userId).orElseThrow(() -> new MessageException("User not found"));
        return mapUserProfile(entity);
    }

    public Profile getUserProfile(String email) {
        User entity = userRepository.findByEmail(email).orElseThrow(() -> new MessageException("User not found"));
        return mapUserProfile(entity);
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

    private Profile mapUserProfile(User entity) {
        return Profile.builder().id(entity.getId()).login(entity.getLogin()).email(entity.getEmail())
                .active(entity.isActive()).isAdmin(entity.getRoles().contains(Role.ADMIN)).build();
    }

}
