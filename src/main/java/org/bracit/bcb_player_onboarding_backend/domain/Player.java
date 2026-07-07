package org.bracit.bcb_player_onboarding_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String id;
    private String name;
    private int age;
    private PlayerPosition position;
    private PlayerStatus status;
    private boolean medicalFitnessPassed;
}
