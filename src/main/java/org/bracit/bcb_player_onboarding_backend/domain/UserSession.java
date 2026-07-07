package org.bracit.bcb_player_onboarding_backend.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {
    private String id;
    private String name;
    private String email;
    private String role; // PLAYER, COACH, ADMIN
    private String phoneNumber;
    private LocalDateTime dob;
    private String divisionId;
    private String districtId;
}
