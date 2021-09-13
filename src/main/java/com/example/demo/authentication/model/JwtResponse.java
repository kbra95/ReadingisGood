package com.example.demo.authentication.model;

public class JwtResponse {
    private final String jwtToken;

    public JwtResponse(String jwttoken) {
        this.jwtToken = jwttoken;
    }

    public String getToken() {
        return this.jwtToken;
    }
}
