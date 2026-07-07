package org.bracit.bcb_player_onboarding_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationDetails {
    private String educationStatusCode;
    private String schoolCollegeId;
    private String schoolClass;
    private String examRollNo;
    private String examRegNo;
    private String examYear;
}
