package com.vcplatform.model;

import java.io.Serializable;

/**
 * JWT 認證響應模型
 */
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private final String jwttoken;
    private final String username;
    private final String role;

    public JwtResponse(String jwttoken, String username, String role) {
        this.jwttoken = jwttoken;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public String getUsername() {
        return this.username;
    }

    public String getRole() {
        return this.role;
    }
} 