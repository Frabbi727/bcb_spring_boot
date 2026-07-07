package org.bracit.bcb_player_onboarding_backend.service;

import org.bracit.bcb_player_onboarding_backend.common.dto.PagedResponse;
import org.bracit.bcb_player_onboarding_backend.dto.PlayerRegisterRequest;
import org.bracit.bcb_player_onboarding_backend.dto.PlayerResponse;

public interface PlayerService {
    PlayerResponse registerPlayer(PlayerRegisterRequest request);
    PlayerResponse getPlayerById(String id);
    PagedResponse<PlayerResponse> searchPlayers(int page, int size, String name, String position, String status);
    PlayerResponse verifyEligibility(String id);
    PlayerResponse updateMedicalStatus(String id, boolean passed);
}
