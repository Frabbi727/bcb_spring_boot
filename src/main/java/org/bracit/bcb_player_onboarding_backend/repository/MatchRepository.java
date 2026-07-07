package org.bracit.bcb_player_onboarding_backend.repository;

import java.util.List;
import java.util.Optional;

import org.bracit.bcb_player_onboarding_backend.domain.Match;

public interface MatchRepository {
    Match save(Match match);
    Optional<Match> findById(String id);
    List<Match> findByCategory(String category);
    List<Match> findAll();
    void deleteById(String id);
}
