package com.shamim.ecommerce.controller;

import com.shamim.ecommerce.constant.AccountStatus;
import com.shamim.ecommerce.dto.request.LoginRequest;
import com.shamim.ecommerce.dto.response.AuthResponse;
import com.shamim.ecommerce.model.Seller;
import com.shamim.ecommerce.model.VerificationCode;
import com.shamim.ecommerce.repository.VerificationCodeRepository;
import com.shamim.ecommerce.service.AuthService;
import com.shamim.ecommerce.service.EmailService;
import com.shamim.ecommerce.service.SellerService;
import com.shamim.ecommerce.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest loginRequest) throws Exception {
        String otp = loginRequest.getOtp();
        String email = loginRequest.getEmail();
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        loginRequest.setEmail("seller_" + email);
        AuthResponse authResponse = authService.signing(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null) {
            throw new Exception("Wrong otp ...");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {
        Seller savedSeller = sellerService.createSeller(seller);
        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "Shamim Email verification code";
        String text = "Welcome to our website. Verify your account using this link ";
        String frontend_url = "http://localhost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus accountStatus) {
        List<Seller> sellers = sellerService.getAllSellers(accountStatus);
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization")  String jwt, @RequestBody Seller seller) throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(),  seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
