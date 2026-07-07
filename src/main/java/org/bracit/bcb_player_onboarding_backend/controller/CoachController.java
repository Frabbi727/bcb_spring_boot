package org.bracit.bcb_player_onboarding_backend.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.bracit.bcb_player_onboarding_backend.common.dto.ApiResponse;
import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerRegistration;
import org.bracit.bcb_player_onboarding_backend.dto.auth.AuthResponse.UserInfo;
import org.bracit.bcb_player_onboarding_backend.dto.coach.CoachActionRequest.*;
import org.bracit.bcb_player_onboarding_backend.service.AuthService;
import org.bracit.bcb_player_onboarding_backend.service.CoachService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coach")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;
    private final AuthService authService;

    private UserInfo getAuthenticatedCoach(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException("Missing or invalid Authorization token");
        }
        String token = authHeader.substring(7).trim();
        UserInfo user = authService.validateAccessToken(token);
        
        if (user == null || !"COACH".equalsIgnoreCase(user.getRole())) {
            throw new BusinessException("Access denied: You must be logged in as a Coach.");
        }
        return user;
    }

    @GetMapping("/registrations")
    public ResponseEntity<ApiResponse<List<PlayerRegistration>>> getRegistrations(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
            
        UserInfo coach = getAuthenticatedCoach(authHeader);
        List<PlayerRegistration> list = coachService.getRegistrationsForCoach(coach.getDistrictId(), status, search);
        return ResponseEntity.ok(ApiResponse.success(list, "Registrations retrieved successfully."));
    }

    @GetMapping("/registrations/{id}")
    public ResponseEntity<ApiResponse<PlayerRegistration>> getRegistrationById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id) {
            
        getAuthenticatedCoach(authHeader);
        PlayerRegistration reg = coachService.getRegistrationById(id);
        return ResponseEntity.ok(ApiResponse.success(reg, "Registration details retrieved."));
    }

    @PostMapping("/registrations/{id}/verify")
    public ResponseEntity<ApiResponse<PlayerRegistration>> verifyPlayer(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @Valid @RequestBody Verify request) {
            
        UserInfo coach = getAuthenticatedCoach(authHeader);
        PlayerRegistration reg = coachService.verifyRegistration(id, request.getPlayerExpertise(), request.getCoachComment(), coach);
        return ResponseEntity.ok(ApiResponse.success(reg, "Registration verified successfully by Coach."));
    }

    @PostMapping("/registrations/{id}/reject")
    public ResponseEntity<ApiResponse<PlayerRegistration>> rejectPlayer(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @Valid @RequestBody Reject request) {
            
        getAuthenticatedCoach(authHeader);
        PlayerRegistration reg = coachService.rejectRegistration(id, request.getFeedbackChips(), request.getComment(), request.getResubmitAfterDate(), request.isPermanent());
        return ResponseEntity.ok(ApiResponse.success(reg, "Registration rejected successfully by Coach."));
    }
}
