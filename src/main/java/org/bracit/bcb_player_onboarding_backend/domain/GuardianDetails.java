package org.bracit.bcb_player_onboarding_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianDetails {
    private String parentNid1Number;
    private String parentNid2Number;
}
