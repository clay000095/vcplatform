package com.vcplatform.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

/**
 * JWT 認證入口點
 * 用於處理未認證的請求
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 當用戶嘗試訪問安全的 REST 資源而不提供任何憑據時，將調用此方法
        // 我們應該發送 401 未授權的響應
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
} 