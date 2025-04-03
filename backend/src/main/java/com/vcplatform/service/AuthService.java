package com.vcplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcplatform.model.User;
import com.vcplatform.repository.UserRepository;

@Service
public class AuthService {
    @Autowired private UserRepository userRepo;
    public boolean register(User user) {
        if (userRepo.existsByEmail(user.getEmail())) return false;
        userRepo.save(user);
        return true;
    }
}
