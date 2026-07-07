package org.bracit.bcb_player_onboarding_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDetails {
    private String faceFrontPath;
    private String faceLeftPath;
    private String faceRightPath;
    private String birthCertificatePath;
    private String playerNidPath;
    private String fatherNidFrontPath;
    private String fatherNidBackPath;
    private String motherNidFrontPath;
    private String motherNidBackPath;
    private String examCertPath;
}
