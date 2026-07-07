package org.bracit.bcb_player_onboarding_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRegisterRequest {
    @NotBlank(message = "Player name must not be blank")
    private String name;

    @Min(value = 18, message = "Minimum age is 18")
    @Max(value = 40, message = "Maximum age is 40")
    private int age;

    @NotBlank(message = "Player position must not be blank")
    private String position;

    @NotNull(message = "Medical fitness check status must be specified")
    private Boolean medicalFitnessPassed;
}
