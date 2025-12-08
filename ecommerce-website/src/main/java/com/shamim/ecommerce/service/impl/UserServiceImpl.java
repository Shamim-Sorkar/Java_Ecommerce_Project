package com.shamim.ecommerce.service.impl;

import com.shamim.ecommerce.config.JwtProvider;
import com.shamim.ecommerce.model.User;
import com.shamim.ecommerce.repository.UserRepository;
import com.shamim.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromToken(jwt);
        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new Exception("User not found wtih email: " + email);
        }
        return user;
    }
}
