package org.bracit.bcb_player_onboarding_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.bracit.bcb_player_onboarding_backend.common.dto.ApiResponse;
import org.bracit.bcb_player_onboarding_backend.common.dto.PagedResponse;
import org.bracit.bcb_player_onboarding_backend.dto.PlayerRegisterRequest;
import org.bracit.bcb_player_onboarding_backend.dto.PlayerResponse;
import org.bracit.bcb_player_onboarding_backend.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<PlayerResponse>> registerPlayer(@Valid @RequestBody PlayerRegisterRequest request) {
        PlayerResponse response = playerService.registerPlayer(request);
        return new ResponseEntity<>(ApiResponse.success(response, "Player registered successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlayerResponse>> getPlayerById(@PathVariable String id) {
        PlayerResponse response = playerService.getPlayerById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Player retrieved successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<PlayerResponse>>> searchPlayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String status) {
            
        PagedResponse<PlayerResponse> response = playerService.searchPlayers(page, size, name, position, status);
        return ResponseEntity.ok(ApiResponse.success(response, "Players list retrieved successfully"));
    }

    @PutMapping("/{id}/verify-eligibility")
    public ResponseEntity<ApiResponse<PlayerResponse>> verifyEligibility(@PathVariable String id) {
        PlayerResponse response = playerService.verifyEligibility(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Player eligibility status verified successfully"));
    }

    @PutMapping("/{id}/medical-status")
    public ResponseEntity<ApiResponse<PlayerResponse>> updateMedicalStatus(
            @PathVariable String id, 
            @RequestParam boolean passed) {
            
        PlayerResponse response = playerService.updateMedicalStatus(id, passed);
        return ResponseEntity.ok(ApiResponse.success(response, "Player medical fitness status updated successfully"));
    }
}
