package com.shamim.ecommerce.dto.request;

import com.shamim.ecommerce.constant.UserRole;
import lombok.Data;

@Data
public class LoginOtpRequest {

    private String email;
    private String otp;
    private UserRole role;
}
