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
public class PersonalDetails {
    private String nameEn;
    private String nameBn;
    private String fatherName;
    private String motherName;
    private LocalDateTime dob;
    private String birthRegistrationNumber;
    private String playerNidNumber;
}
