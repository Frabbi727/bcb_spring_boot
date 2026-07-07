package org.bracit.bcb_player_onboarding_backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordLoginRequest {
    @NotBlank(message = "Identity is required")
    private String identity;

    @NotBlank(message = "Password is required")
    private String password;
}
