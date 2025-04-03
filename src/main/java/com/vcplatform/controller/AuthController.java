package com.vcplatform.controller;


import com.vcplatform.model.User;
import com.vcplatform.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth 認證", description = "帳號註冊與登入")
public class AuthController {

    @Autowired
    private UserService userService;

    @Operation(summary = "使用者註冊", description = "傳入 email / password / role")
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
