package org.bracit.bcb_player_onboarding_backend.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRegistration {
    private String id;
    private String phoneNumber;
    private String status; // draft, pending_approval, under_review, approved, rejected
    
    private PersonalDetails personalDetails;
    private CricketExperience cricketExperience;
    private EducationDetails educationDetails;
    private GuardianDetails guardianDetails;
    private LocationDetails locationDetails;
    private DocumentDetails documentDetails;
    
    private LocalDateTime createdAt;
    
    // Coach Action Fields
    private boolean isCacheVerified;
    private List<String> playerExpertise;
    private String coachComment;
    private String verifiedByCoachId;
    private String verifiedByCoachName;

    // Admin Action Fields
    private String adminComment;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private String playerId; // e.g. U14-2026-0043
    private String qrCodeData;
    
    // Rejection Fields
    private List<String> feedbackChips;
    private String rejectionComment;
    private LocalDateTime resubmitAfterDate;
    private boolean isPermanent;
}
