package com.hotelmanagement.auth.service;

import com.hotelmanagement.auth.dto.JwtResponse;
import com.hotelmanagement.auth.dto.LoginRequest;
import com.hotelmanagement.auth.dto.RegisterRequest;

public interface AuthService {
    void registerUser(RegisterRequest signUpRequest);
    JwtResponse authenticateUser(LoginRequest loginRequest);
}
