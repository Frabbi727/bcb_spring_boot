package org.bracit.bcb_player_onboarding_backend.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;
import org.bracit.bcb_player_onboarding_backend.dto.auth.AuthResponse.*;
import org.bracit.bcb_player_onboarding_backend.dto.auth.OtpVerifyRequest;
import org.bracit.bcb_player_onboarding_backend.dto.auth.PasswordLoginRequest;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {

    // Simple cache for OTP sessions
    private final Map<String, String> activeSessions = new ConcurrentHashMap<>();
    
    // Simple cache for valid tokens
    private final Map<String, String> registrationTokens = new ConcurrentHashMap<>();
    private final Map<String, UserInfo> loggedInUsers = new ConcurrentHashMap<>();

    public OtpSend sendRegistrationOtp(String phoneNumber) {
        log.info("Sending registration OTP to: {}", phoneNumber);
        String sessionId = "reg_otp_sess_" + UUID.randomUUID().toString().substring(0, 8);
        activeSessions.put(sessionId, phoneNumber);
        return new OtpSend(sessionId, 120);
    }

    public OtpVerifyRegistration verifyRegistrationOtp(OtpVerifyRequest request) {
        log.info("Verifying registration OTP for: {}", request.getPhoneNumber());
        String cachedNumber = activeSessions.get(request.getSessionId());
        
        if (cachedNumber == null || !cachedNumber.equals(request.getPhoneNumber())) {
            throw new BusinessException("Invalid or expired session ID");
        }
        
        // Match default test OTP code
        if (!"123456".equals(request.getOtp())) {
            throw new BusinessException("Invalid OTP code. Please use '123456' for local verification.");
        }

        activeSessions.remove(request.getSessionId());
        String regToken = "temp_reg_token_" + UUID.randomUUID().toString().substring(0, 12);
        registrationTokens.put(regToken, request.getPhoneNumber());
        
        return new OtpVerifyRegistration(regToken, request.getPhoneNumber());
    }

    public OtpSend sendLoginOtp(String phoneNumber) {
        log.info("Sending login OTP to: {}", phoneNumber);
        String sessionId = "login_otp_sess_" + UUID.randomUUID().toString().substring(0, 8);
        activeSessions.put(sessionId, phoneNumber);
        return new OtpSend(sessionId, 120);
    }

    public Login verifyLoginOtp(OtpVerifyRequest request) {
        log.info("Verifying login OTP for: {}", request.getPhoneNumber());
        String cachedNumber = activeSessions.get(request.getSessionId());
        
        if (cachedNumber == null || !cachedNumber.equals(request.getPhoneNumber())) {
            throw new BusinessException("Invalid or expired session ID");
        }
        
        // Match default test OTP code
        if (!"654321".equals(request.getOtp()) && !"123456".equals(request.getOtp())) {
            throw new BusinessException("Invalid OTP code. Please use '654321' or '123456' for local verification.");
        }

        activeSessions.remove(request.getSessionId());

        UserInfo user = UserInfo.builder()
                .id("usr_" + UUID.randomUUID().toString().substring(0, 4))
                .name("Standard User (" + request.getPhoneNumber() + ")")
                .email("user@bcb.gov.bd")
                .role("COACH") // Default to COACH for logins
                .phoneNumber(request.getPhoneNumber())
                .dob(LocalDateTime.of(1988, 3, 1, 0, 0))
                .divisionId("div_dhaka")
                .districtId("dist_dhaka")
                .build();

        String accessToken = "access_token_" + UUID.randomUUID().toString().substring(0, 16);
        String refreshToken = "refresh_token_" + UUID.randomUUID().toString().substring(0, 16);
        loggedInUsers.put(accessToken, user);

        return new Login(accessToken, refreshToken, 3600, user);
    }

    public Login passwordLogin(PasswordLoginRequest request) {
        log.info("Processing password login for identity: {}", request.getIdentity());

        UserInfo user;
        if ("admin@example.com".equalsIgnoreCase(request.getIdentity()) && "password".equals(request.getPassword())) {
            user = UserInfo.builder()
                    .id("usr_admin")
                    .name("Super Administrator")
                    .email("admin@example.com")
                    .role("ADMIN")
                    .phoneNumber("+8801700000001")
                    .dob(LocalDateTime.of(1985, 1, 1, 0, 0))
                    .build();
        } else if ("coach@example.com".equalsIgnoreCase(request.getIdentity()) && "password".equals(request.getPassword())) {
            user = UserInfo.builder()
                    .id("usr_coach")
                    .name("Coach District Head")
                    .email("coach@example.com")
                    .role("COACH")
                    .phoneNumber("+8801863098727")
                    .dob(LocalDateTime.of(1988, 3, 1, 0, 0))
                    .divisionId("div_dhaka")
                    .districtId("dist_dhaka")
                    .build();
        } else {
            throw new BusinessException("Invalid identity or password credentials.");
        }

        String accessToken = "access_token_" + UUID.randomUUID().toString().substring(0, 16);
        String refreshToken = "refresh_token_" + UUID.randomUUID().toString().substring(0, 16);
        loggedInUsers.put(accessToken, user);

        return new Login(accessToken, refreshToken, 3600, user);
    }

    public Login refreshToken(String refreshTokenStr) {
        log.info("Refreshing access token");
        UserInfo user = UserInfo.builder()
                .id("usr_refreshed")
                .name("Refreshed Session User")
                .email("session@bcb.gov.bd")
                .role("COACH")
                .build();
        String accessToken = "access_token_" + UUID.randomUUID().toString().substring(0, 16);
        String newRefreshToken = "refresh_token_" + UUID.randomUUID().toString().substring(0, 16);
        loggedInUsers.put(accessToken, user);
        return new Login(accessToken, newRefreshToken, 3600, user);
    }

    public void logout(String accessToken) {
        log.info("Invalidating user session");
        if (accessToken != null) {
            String tokenClean = accessToken.replace("Bearer ", "").trim();
            loggedInUsers.remove(tokenClean);
        }
    }

    public boolean validateRegistrationToken(String token) {
        return token != null && registrationTokens.containsKey(token.trim());
    }

    public String getPhoneNumberFromRegToken(String token) {
        if (token == null) return null;
        return registrationTokens.get(token.trim());
    }

    public UserInfo validateAccessToken(String token) {
        if (token == null) return null;
        String cleanToken = token.replace("Bearer ", "").trim();
        return loggedInUsers.get(cleanToken);
    }
}
