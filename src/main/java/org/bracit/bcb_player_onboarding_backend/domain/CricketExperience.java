package org.bracit.bcb_player_onboarding_backend.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CricketExperience {
    private String preferredBattingPositionCode;
    private String primaryBowlingStyleCode;
    private String primaryRoleCode;
    private boolean isWicketkeeper;
    private String highestCompetitiveLevelCode;
    private Integer yearsOfExperience;
    private String academyClub;
    private List<String> participationRecord;
    private String participationEligibility;
}
