package com.shamim.ecommerce.controller;

import com.shamim.ecommerce.constant.UserRole;
import com.shamim.ecommerce.dto.request.SignupRequest;
import com.shamim.ecommerce.dto.response.ApiResponse;
import com.shamim.ecommerce.dto.response.AuthResponse;
import com.shamim.ecommerce.model.User;
import com.shamim.ecommerce.model.VerificationCode;
import com.shamim.ecommerce.repository.UserRepository;
import com.shamim.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest signupRequest) throws Exception {
        String jwt = authService.createUser(signupRequest);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("register success");
        authResponse.setRole(UserRole.ROLE_CUSTOMER);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody VerificationCode verificationCode) throws Exception {
        authService.sentLoginOtp(verificationCode.getEmail());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Otp sent successfully");
        return ResponseEntity.ok(apiResponse);
    }
}
