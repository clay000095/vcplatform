package com.vcplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vcplatform.repository.UserRepository;
import com.vcplatform.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return false;
        }
        userRepository.save(user);
        return true;
    }
}
