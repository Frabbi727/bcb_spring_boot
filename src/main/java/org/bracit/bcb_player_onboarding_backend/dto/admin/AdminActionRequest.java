package org.bracit.bcb_player_onboarding_backend.dto.admin;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AdminActionRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Approve {
        @NotBlank(message = "Admin comment is required")
        private String adminComment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reject {
        private List<String> feedbackChips;
        
        @NotBlank(message = "Rejection comment is required")
        private String comment;
        
        private boolean isPermanent;
    }
}
