package com.vcplatform.controller;


import com.vcplatform.model.User;
import com.vcplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        boolean success = userService.register(user);
        if (success) {
            return ResponseEntity.ok("註冊成功！");
        } else {
            return ResponseEntity.badRequest().body("Email 已被註冊");
        }
    }
}
