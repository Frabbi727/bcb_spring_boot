package org.bracit.bcb_player_onboarding_backend.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bracit.bcb_player_onboarding_backend.common.dto.ApiResponse;
import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;
import org.bracit.bcb_player_onboarding_backend.domain.*;
import org.bracit.bcb_player_onboarding_backend.service.AuthService;
import org.bracit.bcb_player_onboarding_backend.service.PlayerRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerRegistrationController {

    private final PlayerRegistrationService registrationService;
    private final AuthService authService;

    private String getPhoneNumber(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException("Missing or invalid Authorization token");
        }
        String token = authHeader.substring(7).trim();
        
        // Try to validate as temporary registration token
        if (authService.validateRegistrationToken(token)) {
            return authService.getPhoneNumberFromRegToken(token);
        }
        
        // Try to validate as full login access token
        var userInfo = authService.validateAccessToken(token);
        if (userInfo != null) {
            return userInfo.getPhoneNumber();
        }
        
        throw new BusinessException("Invalid or expired session token");
    }

    @GetMapping("/registration/draft")
    public ResponseEntity<ApiResponse<PlayerRegistration>> getDraft(@RequestHeader("Authorization") String authHeader) {
        String phone = getPhoneNumber(authHeader);
        PlayerRegistration draft = registrationService.getOrCreateDraft(phone);
        return ResponseEntity.ok(ApiResponse.success(draft, "Draft retrieved successfully."));
    }

    @PutMapping("/registration/personal")
    public ResponseEntity<ApiResponse<Map<String, String>>> savePersonal(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PersonalDetails details) {
            
        String phone = getPhoneNumber(authHeader);
        PlayerRegistration draft = registrationService.savePersonal(phone, details);
        
        Map<String, String> data = new HashMap<>();
        data.put("id", draft.getId());
        data.put("status", draft.getStatus());
        
        return ResponseEntity.ok(ApiResponse.success(data, "Personal info saved successfully."));
    }

    @PutMapping("/registration/cricket")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveCricket(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CricketExperience experience) {
            
        String phone = getPhoneNumber(authHeader);
        PlayerRegistration draft = registrationService.saveCricket(phone, experience);
        
        Map<String, String> data = new HashMap<>();
        data.put("id", draft.getId());
        data.put("status", draft.getStatus());
        
        return ResponseEntity.ok(ApiResponse.success(data, "Cricket experience saved successfully."));
    }

    @PutMapping("/registration/education")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveEducation(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EducationDetails details) {
            
        String phone = getPhoneNumber(authHeader);
        PlayerRegistration draft = registrationService.saveEducation(phone, details);
        
        Map<String, String> data = new HashMap<>();
        data.put("id", draft.getId());
        data.put("status", draft.getStatus());
        
        return ResponseEntity.ok(ApiResponse.success(data, "Education details saved successfully."));
    }

    @PutMapping("/registration/guardian")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveGuardian(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody GuardianDetails details) {
            
        String phone = getPhoneNumber(authHeader);
        PlayerRegistration draft = registrationService.saveGuardian(phone, details);
        
        Map<String, String> data = new HashMap<>();
        data.put("id", draft.getId());
        data.put("status", draft.getStatus());
        
        return ResponseEntity.ok(ApiResponse.success(data, "Guardian info saved successfully."));
    }

    @PutMapping("/registration/location")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveLocation(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LocationDetails details) {
            
        String phone = getPhoneNumber(authHeader);
        PlayerRegistration draft = registrationService.saveLocation(phone, details);
        
        Map<String, String> data = new HashMap<>();
        data.put("id", draft.getId());
        data.put("status", draft.getStatus());
        
        return ResponseEntity.ok(ApiResponse.success(data, "Location info saved successfully."));
    }

    @PostMapping("/registration/documents")
    public ResponseEntity<ApiResponse<DocumentDetails>> uploadDocuments(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "faceFront", required = false) MultipartFile faceFront,
            @RequestParam(value = "faceLeft", required = false) MultipartFile faceLeft,
            @RequestParam(value = "faceRight", required = false) MultipartFile faceRight,
            @RequestParam(value = "birthCertificate", required = false) MultipartFile birthCertificate,
            @RequestParam(value = "playerNid", required = false) MultipartFile playerNid,
            @RequestParam(value = "fatherNidFront", required = false) MultipartFile fatherNidFront,
            @RequestParam(value = "fatherNidBack", required = false) MultipartFile fatherNidBack,
            @RequestParam(value = "motherNidFront", required = false) MultipartFile motherNidFront,
            @RequestParam(value = "motherNidBack", required = false) MultipartFile motherNidBack,
            @RequestParam(value = "educationCert", required = false) MultipartFile educationCert) {
            
        String phone = getPhoneNumber(authHeader);
        
        // Simulating upload paths
        DocumentDetails docs = DocumentDetails.builder()
                .faceFrontPath(faceFront != null ? "https://storage.bcb.gov.bd/players/usr_100/face_front.jpg" : null)
                .faceLeftPath(faceLeft != null ? "https://storage.bcb.gov.bd/players/usr_100/face_left.jpg" : null)
                .faceRightPath(faceRight != null ? "https://storage.bcb.gov.bd/players/usr_100/face_right.jpg" : null)
                .birthCertificatePath(birthCertificate != null ? "https://storage.bcb.gov.bd/players/usr_100/birth_certificate.jpg" : null)
                .playerNidPath(playerNid != null ? "https://storage.bcb.gov.bd/players/usr_100/player_nid.jpg" : null)
                .fatherNidFrontPath(fatherNidFront != null ? "https://storage.bcb.gov.bd/players/usr_100/father_nid_front.jpg" : null)
                .fatherNidBackPath(fatherNidBack != null ? "https://storage.bcb.gov.bd/players/usr_100/father_nid_back.jpg" : null)
                .motherNidFrontPath(motherNidFront != null ? "https://storage.bcb.gov.bd/players/usr_100/mother_nid_front.jpg" : null)
                .motherNidBackPath(motherNidBack != null ? "https://storage.bcb.gov.bd/players/usr_100/mother_nid_back.jpg" : null)
                .examCertPath(educationCert != null ? "https://storage.bcb.gov.bd/players/usr_100/exam_cert.jpg" : null)
                .build();
                
        registrationService.saveDocuments(phone, docs);
        return ResponseEntity.ok(ApiResponse.success(docs, "Documents uploaded successfully."));
    }

    @PostMapping("/registration/submit")
    public ResponseEntity<ApiResponse<Map<String, Object>>> submitDraft(@RequestHeader("Authorization") String authHeader) {
        String phone = getPhoneNumber(authHeader);
        PlayerRegistration reg = registrationService.submitRegistration(phone);
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", reg.getId());
        data.put("status", reg.getStatus());
        data.put("createdAt", reg.getCreatedAt());
        
        return ResponseEntity.ok(ApiResponse.success(data, "Registration submitted successfully for review."));
    }

    @DeleteMapping("/registration")
    public ResponseEntity<ApiResponse<Void>> discardDraft(@RequestHeader("Authorization") String authHeader) {
        String phone = getPhoneNumber(authHeader);
        registrationService.discardDraft(phone);
        return ResponseEntity.ok(ApiResponse.success(null, "Draft discarded."));
    }

    @GetMapping("/registrations")
    public ResponseEntity<ApiResponse<List<PlayerRegistration>>> getHistory(@RequestHeader("Authorization") String authHeader) {
        String phone = getPhoneNumber(authHeader);
        List<PlayerRegistration> history = registrationService.getRegistrationHistory(phone);
        return ResponseEntity.ok(ApiResponse.success(history, "Registration history retrieved."));
    }

    @GetMapping("/registration/status")
    public ResponseEntity<ApiResponse<String>> getStatus(@RequestHeader("Authorization") String authHeader) {
        String phone = getPhoneNumber(authHeader);
        String status = registrationService.getLatestRegistrationStatus(phone);
        return ResponseEntity.ok(ApiResponse.success(status, "Status retrieved."));
    }

    @GetMapping("/matches")
    public ResponseEntity<ApiResponse<List<Match>>> getMatches(
            @RequestParam(required = false, defaultValue = "U14") String category) {
            
        List<Match> matches = registrationService.getMatchesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(matches, "Matches retrieved."));
    }
}
