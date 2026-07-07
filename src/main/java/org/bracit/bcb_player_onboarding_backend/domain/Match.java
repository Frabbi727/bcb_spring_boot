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
public class Match {
    private String id;
    private String title;
    private String category; // U14, U16, U19
    private String venue;
    private LocalDateTime dateTime;
    private String opponent;
}
