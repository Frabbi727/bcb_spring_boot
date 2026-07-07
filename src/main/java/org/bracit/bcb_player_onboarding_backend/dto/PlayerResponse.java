package org.bracit.bcb_player_onboarding_backend.dto;

import org.bracit.bcb_player_onboarding_backend.domain.Player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResponse {
    private String id;
    private String name;
    private int age;
    private String position;
    private String status;
    private boolean medicalFitnessPassed;

    public static PlayerResponse fromDomain(Player player) {
        if (player == null) return null;
        return PlayerResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .age(player.getAge())
                .position(player.getPosition() != null ? player.getPosition().name() : null)
                .status(player.getStatus() != null ? player.getStatus().name() : null)
                .medicalFitnessPassed(player.isMedicalFitnessPassed())
                .build();
    }
}
