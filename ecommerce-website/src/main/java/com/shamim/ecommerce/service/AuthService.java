package com.shamim.ecommerce.service;

import com.shamim.ecommerce.constant.UserRole;
import com.shamim.ecommerce.dto.request.LoginRequest;
import com.shamim.ecommerce.dto.request.SignupRequest;
import com.shamim.ecommerce.dto.response.AuthResponse;

public interface AuthService {

    void sentLoginOtp(String email, UserRole role) throws Exception;
    String createUser(SignupRequest signupRequest) throws Exception;
    AuthResponse signing(LoginRequest loginRequest);
}
