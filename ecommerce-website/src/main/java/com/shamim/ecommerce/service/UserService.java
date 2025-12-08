package com.shamim.ecommerce.service;

import com.shamim.ecommerce.model.User;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
