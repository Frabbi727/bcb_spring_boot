package org.bracit.bcb_player_onboarding_backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.bracit.bcb_player_onboarding_backend.common.dto.ApiResponse;
import org.bracit.bcb_player_onboarding_backend.common.dto.PagedResponse;
import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;
import org.bracit.bcb_player_onboarding_backend.domain.Match;
import org.bracit.bcb_player_onboarding_backend.domain.MetadataModels.*;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerRegistration;
import org.bracit.bcb_player_onboarding_backend.domain.SponsorBanner;
import org.bracit.bcb_player_onboarding_backend.dto.admin.AdminActionRequest.*;
import org.bracit.bcb_player_onboarding_backend.dto.auth.AuthResponse.UserInfo;
import org.bracit.bcb_player_onboarding_backend.service.AdminService;
import org.bracit.bcb_player_onboarding_backend.service.AuthService;
import org.bracit.bcb_player_onboarding_backend.service.MetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MetadataService metadataService;
    private final AuthService authService;

    private UserInfo getAuthenticatedAdmin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException("Missing or invalid Authorization token");
        }
        String token = authHeader.substring(7).trim();
        UserInfo user = authService.validateAccessToken(token);
        
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new BusinessException("Access denied: You must be logged in as an Admin.");
        }
        return user;
    }

    // Dynamic Metadata CRUD
    @PostMapping("/metadata/playing-roles")
    public ResponseEntity<ApiResponse<PlayingRole>> createPlayingRole(
            @RequestHeader("Authorization") String authHeader, @RequestBody PlayingRole role) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.createPlayingRole(role), "Role created."));
    }

    @PutMapping("/metadata/playing-roles/{code}")
    public ResponseEntity<ApiResponse<PlayingRole>> updatePlayingRole(
            @RequestHeader("Authorization") String authHeader, @PathVariable String code, @RequestBody PlayingRole role) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.updatePlayingRole(code, role), "Role updated."));
    }

    @DeleteMapping("/metadata/playing-roles/{code}")
    public ResponseEntity<ApiResponse<Void>> deletePlayingRole(
            @RequestHeader("Authorization") String authHeader, @PathVariable String code) {
        getAuthenticatedAdmin(authHeader);
        metadataService.deletePlayingRole(code);
        return ResponseEntity.ok(ApiResponse.success(null, "Role deleted."));
    }

    // Dynamic Batting Positions CRUD
    @PostMapping("/metadata/batting-positions")
    public ResponseEntity<ApiResponse<BattingPosition>> createBattingPosition(
            @RequestHeader("Authorization") String authHeader, @RequestBody BattingPosition pos) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.createBattingPosition(pos), "Position created."));
    }

    @PutMapping("/metadata/batting-positions/{code}")
    public ResponseEntity<ApiResponse<BattingPosition>> updateBattingPosition(
            @RequestHeader("Authorization") String authHeader, @PathVariable String code, @RequestBody BattingPosition pos) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.updateBattingPosition(code, pos), "Position updated."));
    }

    @DeleteMapping("/metadata/batting-positions/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteBattingPosition(
            @RequestHeader("Authorization") String authHeader, @PathVariable String code) {
        getAuthenticatedAdmin(authHeader);
        metadataService.deleteBattingPosition(code);
        return ResponseEntity.ok(ApiResponse.success(null, "Position deleted."));
    }

    // Dynamic Bowling Styles CRUD
    @PostMapping("/metadata/bowling-styles")
    public ResponseEntity<ApiResponse<BowlingStyle>> createBowlingStyle(
            @RequestHeader("Authorization") String authHeader, @RequestBody BowlingStyle style) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.createBowlingStyle(style), "Bowling style created."));
    }

    @PutMapping("/metadata/bowling-styles/{code}")
    public ResponseEntity<ApiResponse<BowlingStyle>> updateBowlingStyle(
            @RequestHeader("Authorization") String authHeader, @PathVariable String code, @RequestBody BowlingStyle style) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.updateBowlingStyle(code, style), "Bowling style updated."));
    }

    @DeleteMapping("/metadata/bowling-styles/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteBowlingStyle(
            @RequestHeader("Authorization") String authHeader, @PathVariable String code) {
        getAuthenticatedAdmin(authHeader);
        metadataService.deleteBowlingStyle(code);
        return ResponseEntity.ok(ApiResponse.success(null, "Bowling style deleted."));
    }

    // Dynamic Competitive Levels CRUD
    @PostMapping("/metadata/competitive-levels")
    public ResponseEntity<ApiResponse<CompetitiveLevel>> createCompetitiveLevel(
            @RequestHeader("Authorization") String authHeader, @RequestBody CompetitiveLevel level) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.createCompetitiveLevel(level), "Competitive level created."));
    }

    @PutMapping("/metadata/competitive-levels/{code}")
    public ResponseEntity<ApiResponse<CompetitiveLevel>> updateCompetitiveLevel(
            @RequestHeader("Authorization") String authHeader, @PathVariable String code, @RequestBody CompetitiveLevel level) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.updateCompetitiveLevel(code, level), "Competitive level updated."));
    }

    @DeleteMapping("/metadata/competitive-levels/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteCompetitiveLevel(
            @RequestHeader("Authorization") String authHeader, @PathVariable String code) {
        getAuthenticatedAdmin(authHeader);
        metadataService.deleteCompetitiveLevel(code);
        return ResponseEntity.ok(ApiResponse.success(null, "Competitive level deleted."));
    }

    // Dynamic Education Statuses CRUD
    @PostMapping("/metadata/education-statuses")
    public ResponseEntity<ApiResponse<EducationStatus>> createEducationStatus(
            @RequestHeader("Authorization") String authHeader, @RequestBody EducationStatus status) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.createEducationStatus(status), "Education status created."));
    }

    @PutMapping("/metadata/education-statuses/{code}")
    public ResponseEntity<ApiResponse<EducationStatus>> updateEducationStatus(
            @RequestHeader("Authorization") String authHeader, @PathVariable String code, @RequestBody EducationStatus status) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.updateEducationStatus(code, status), "Education status updated."));
    }

    @DeleteMapping("/metadata/education-statuses/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteEducationStatus(
            @RequestHeader("Authorization") String authHeader, @PathVariable String code) {
        getAuthenticatedAdmin(authHeader);
        metadataService.deleteEducationStatus(code);
        return ResponseEntity.ok(ApiResponse.success(null, "Education status deleted."));
    }

    // Dynamic Schools CRUD
    @PostMapping("/metadata/schools")
    public ResponseEntity<ApiResponse<School>> createSchool(
            @RequestHeader("Authorization") String authHeader, @RequestBody School school) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.createSchool(school), "School created."));
    }

    @PutMapping("/metadata/schools/{id}")
    public ResponseEntity<ApiResponse<School>> updateSchool(
            @RequestHeader("Authorization") String authHeader, @PathVariable String id, @RequestBody School school) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.updateSchool(id, school), "School updated."));
    }

    @DeleteMapping("/metadata/schools/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSchool(
            @RequestHeader("Authorization") String authHeader, @PathVariable String id) {
        getAuthenticatedAdmin(authHeader);
        metadataService.deleteSchool(id);
        return ResponseEntity.ok(ApiResponse.success(null, "School deleted."));
    }

    // Location Dynamic CRUD
    @PostMapping("/metadata/locations/divisions")
    public ResponseEntity<ApiResponse<Division>> createDivision(
            @RequestHeader("Authorization") String authHeader, @RequestBody Division division) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.createDivision(division), "Division node added."));
    }

    @PostMapping("/metadata/locations/districts")
    public ResponseEntity<ApiResponse<District>> createDistrict(
            @RequestHeader("Authorization") String authHeader, @RequestParam String divisionId, @RequestBody District district) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.createDistrict(divisionId, district), "District node added."));
    }

    @PostMapping("/metadata/locations/upazilas")
    public ResponseEntity<ApiResponse<Upazila>> createUpazila(
            @RequestHeader("Authorization") String authHeader, @RequestParam String districtId, @RequestBody Upazila upazila) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.createUpazila(districtId, upazila), "Upazila node added."));
    }

    @PostMapping("/metadata/locations/unions")
    public ResponseEntity<ApiResponse<Union>> createUnion(
            @RequestHeader("Authorization") String authHeader, @RequestParam String upazilaId, @RequestBody Union union) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(metadataService.createUnion(upazilaId, union), "Union node added."));
    }

    @PutMapping("/metadata/locations/{nodeType}/{id}")
    public ResponseEntity<ApiResponse<Void>> updateLocation(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String nodeType,
            @PathVariable String id,
            @RequestBody LocationUpdateBody body) {
            
        getAuthenticatedAdmin(authHeader);
        metadataService.updateLocation(nodeType, id, body.getNameEn(), body.getNameBn());
        return ResponseEntity.ok(ApiResponse.success(null, "Location node updated."));
    }

    @DeleteMapping("/metadata/locations/{nodeType}/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLocation(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String nodeType,
            @PathVariable String id) {
            
        getAuthenticatedAdmin(authHeader);
        metadataService.deleteLocation(nodeType, id);
        return ResponseEntity.ok(ApiResponse.success(null, "Location node deleted."));
    }

    // Global Registration Oversight
    @GetMapping("/registrations")
    public ResponseEntity<ApiResponse<PagedResponse<PlayerRegistration>>> getRegistrations(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String districtId,
            @RequestParam(required = false) String divisionId,
            @RequestParam(required = false) String search) {
            
        getAuthenticatedAdmin(authHeader);
        PagedResponse<PlayerRegistration> list = adminService.getRegistrationsForAdmin(page, size, status, districtId, divisionId, search);
        return ResponseEntity.ok(ApiResponse.success(list, "Registrations list retrieved."));
    }

    @GetMapping("/registrations/{id}")
    public ResponseEntity<ApiResponse<PlayerRegistration>> getRegistrationById(
            @RequestHeader("Authorization") String authHeader, @PathVariable String id) {
        getAuthenticatedAdmin(authHeader);
        PlayerRegistration reg = adminService.getRegistrationById(id);
        return ResponseEntity.ok(ApiResponse.success(reg, "Registration details retrieved."));
    }

    // Final Approval and Rejection
    @PostMapping("/registrations/{id}/approve")
    public ResponseEntity<ApiResponse<PlayerRegistration>> approvePlayer(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @Valid @RequestBody Approve request) {
            
        UserInfo admin = getAuthenticatedAdmin(authHeader);
        PlayerRegistration reg = adminService.approveRegistration(id, request.getAdminComment(), admin);
        return ResponseEntity.ok(ApiResponse.success(reg, "Registration approved successfully by Admin."));
    }

    @PostMapping("/registrations/{id}/reject")
    public ResponseEntity<ApiResponse<PlayerRegistration>> rejectPlayer(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @Valid @RequestBody Reject request) {
            
        getAuthenticatedAdmin(authHeader);
        PlayerRegistration reg = adminService.rejectRegistration(id, request.getFeedbackChips(), request.getComment(), request.isPermanent());
        return ResponseEntity.ok(ApiResponse.success(reg, "Registration rejected successfully by Admin."));
    }

    // Match Calendar Management CRUD
    @PostMapping("/matches")
    public ResponseEntity<ApiResponse<Match>> createMatch(
            @RequestHeader("Authorization") String authHeader, @RequestBody Match match) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(adminService.createMatch(match), "Match scheduled successfully."));
    }

    @PutMapping("/matches/{id}")
    public ResponseEntity<ApiResponse<Match>> updateMatch(
            @RequestHeader("Authorization") String authHeader, @PathVariable String id, @RequestBody Match match) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(adminService.updateMatch(id, match), "Match updated successfully."));
    }

    @DeleteMapping("/matches/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMatch(
            @RequestHeader("Authorization") String authHeader, @PathVariable String id) {
        getAuthenticatedAdmin(authHeader);
        adminService.deleteMatch(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Match deleted."));
    }

    // Sponsor Banners Management CRUD
    @PostMapping("/banners")
    public ResponseEntity<ApiResponse<SponsorBanner>> createBanner(
            @RequestHeader("Authorization") String authHeader, @RequestBody SponsorBanner banner) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(adminService.createBanner(banner), "Banner created."));
    }

    @PutMapping("/banners/{id}")
    public ResponseEntity<ApiResponse<SponsorBanner>> updateBanner(
            @RequestHeader("Authorization") String authHeader, @PathVariable String id, @RequestBody SponsorBanner banner) {
        getAuthenticatedAdmin(authHeader);
        return ResponseEntity.ok(ApiResponse.success(adminService.updateBanner(id, banner), "Banner updated."));
    }

    @DeleteMapping("/banners/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBanner(
            @RequestHeader("Authorization") String authHeader, @PathVariable String id) {
        getAuthenticatedAdmin(authHeader);
        adminService.deleteBanner(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Banner deleted."));
    }

    @Data
    public static class LocationUpdateBody {
        private String nameEn;
        private String nameBn;
    }
}
