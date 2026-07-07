package org.bracit.bcb_player_onboarding_backend.controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.bracit.bcb_player_onboarding_backend.common.dto.ApiResponse;
import org.bracit.bcb_player_onboarding_backend.dto.auth.AuthResponse.*;
import org.bracit.bcb_player_onboarding_backend.dto.auth.OtpSendRequest;
import org.bracit.bcb_player_onboarding_backend.dto.auth.OtpVerifyRequest;
import org.bracit.bcb_player_onboarding_backend.dto.auth.PasswordLoginRequest;
import org.bracit.bcb_player_onboarding_backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/otp/send")
    public ResponseEntity<ApiResponse<OtpSend>> sendRegisterOtp(@Valid @RequestBody OtpSendRequest request) {
        OtpSend data = authService.sendRegistrationOtp(request.getPhoneNumber());
        return ResponseEntity.ok(ApiResponse.success(data, "OTP sent successfully."));
    }

    @PostMapping("/register/otp/verify")
    public ResponseEntity<ApiResponse<OtpVerifyRegistration>> verifyRegisterOtp(@Valid @RequestBody OtpVerifyRequest request) {
        OtpVerifyRegistration data = authService.verifyRegistrationOtp(request);
        return ResponseEntity.ok(ApiResponse.success(data, "OTP verified successfully. Proceed to step-by-step registration."));
    }

    @PostMapping("/login/otp/send")
    public ResponseEntity<ApiResponse<OtpSend>> sendLoginOtp(@Valid @RequestBody OtpSendRequest request) {
        OtpSend data = authService.sendLoginOtp(request.getPhoneNumber());
        return ResponseEntity.ok(ApiResponse.success(data, "OTP sent successfully."));
    }

    @PostMapping("/login/otp/verify")
    public ResponseEntity<ApiResponse<Login>> verifyLoginOtp(@Valid @RequestBody OtpVerifyRequest request) {
        Login data = authService.verifyLoginOtp(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Login successful."));
    }

    @PostMapping("/login/password")
    public ResponseEntity<ApiResponse<Login>> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        Login data = authService.passwordLogin(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Login successful."));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Login>> refresh(@RequestBody MapWrapper body) {
        Login data = authService.refreshToken(body.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(data, "Session refreshed."));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        authService.logout(token);
        return ResponseEntity.ok(ApiResponse.success(null, "Logout successful."));
    }

    @Data
    public static class MapWrapper {
        private String refreshToken;
    }
}
