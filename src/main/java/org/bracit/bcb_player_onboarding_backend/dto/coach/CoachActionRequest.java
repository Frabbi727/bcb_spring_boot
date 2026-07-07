package org.bracit.bcb_player_onboarding_backend.dto.coach;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CoachActionRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Verify {
        @NotEmpty(message = "Player expertise tags are required")
        private List<String> playerExpertise;

        @NotBlank(message = "Coach comment is required")
        private String coachComment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reject {
        private List<String> feedbackChips;
        
        @NotBlank(message = "Rejection comment is required")
        private String comment;
        
        private LocalDateTime resubmitAfterDate;
        private boolean isPermanent;
    }
}
