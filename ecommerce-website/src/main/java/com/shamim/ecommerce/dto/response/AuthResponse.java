package com.shamim.ecommerce.dto.response;

import com.shamim.ecommerce.constant.UserRole;
import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private String message;
    private UserRole role;
}
