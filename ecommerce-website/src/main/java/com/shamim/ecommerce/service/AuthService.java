package com.shamim.ecommerce.service;

import com.shamim.ecommerce.dto.request.SignupRequest;

public interface AuthService {

    void sentLoginOtp(String email) throws Exception;
    String createUser(SignupRequest signupRequest) throws Exception;
}
