package org.bracit.bcb_player_onboarding_backend.repository;

import java.util.List;
import java.util.Optional;

import org.bracit.bcb_player_onboarding_backend.domain.Player;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerPosition;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerStatus;

public interface PlayerRepository {
    Player save(Player player);
    Optional<Player> findById(String id);
    List<Player> findAll(int page, int size, String name, PlayerPosition position, PlayerStatus status);
    long count(String name, PlayerPosition position, PlayerStatus status);
    void deleteById(String id);
}
