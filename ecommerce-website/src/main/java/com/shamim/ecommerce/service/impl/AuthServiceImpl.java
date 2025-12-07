package com.shamim.ecommerce.service.impl;

import com.shamim.ecommerce.config.JwtProvider;
import com.shamim.ecommerce.constant.UserRole;
import com.shamim.ecommerce.dto.request.SignupRequest;
import com.shamim.ecommerce.model.Cart;
import com.shamim.ecommerce.model.User;
import com.shamim.ecommerce.model.VerificationCode;
import com.shamim.ecommerce.repository.CartRepository;
import com.shamim.ecommerce.repository.UserRepository;
import com.shamim.ecommerce.repository.VerificationCodeRepository;
import com.shamim.ecommerce.service.AuthService;
import com.shamim.ecommerce.service.EmailService;
import com.shamim.ecommerce.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    @Override
    public void sentLoginOtp(String email) throws Exception {
        String SIGNING_PREFIX = "signin_";
        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new Exception("User not exist with provided email");
            }
        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if (isExist != null) {
            verificationCodeRepository.delete(isExist);
        }

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);
        verificationCodeRepository.save(verificationCode);

        String subject = "login/signup otp";
        String text = "Your login/signup otp has been sent. otp is: " + otp;
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Override
    public String createUser(SignupRequest signupRequest) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(signupRequest.getEmail());
        if (verificationCode == null || !verificationCode.equals(signupRequest.getOtp())) {
            throw new Exception("Wrong otp....");
        }

        User user = userRepository.findByEmail(signupRequest.getEmail());
        if (user  == null) {
            User createdUser  = new User();
            createdUser .setEmail(signupRequest.getEmail());
            createdUser .setFullName(signupRequest.getFullName());
            createdUser .setRole(UserRole.ROLE_CUSTOMER);
            createdUser.setEmail("213214124");
            createdUser.setPassword(passwordEncoder.encode(signupRequest.getOtp()));
            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(signupRequest.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }
}
