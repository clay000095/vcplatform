package com.vcplatform.controller;


import com.vcplatform.model.User;
import com.vcplatform.service.UserService;
import com.vcplatform.config.JwtConfig;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth 認證", description = "帳號註冊與登入")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtConfig jwtConfig;

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

    @Operation(summary = "使用者登入", description = "傳入 email / password")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return userService.findByEmail(user.getEmail())
                .map(foundUser -> {
                    if (foundUser.getPassword().equals(user.getPassword())) {
                        // 生成JWT token
                        String token = jwtConfig.generateToken(foundUser.getEmail(), foundUser.getRole());
                        
                        // 創建響應對象
                        Map<String, Object> response = new HashMap<>();
                        response.put("token", token);
                        response.put("user", foundUser);
                        
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body("密碼錯誤");
                    }
                })
                .orElse(ResponseEntity.badRequest().body("用戶不存在"));
    }
    
    @Operation(summary = "使用者登出", description = "登出系統")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("登出成功");
    }
}
