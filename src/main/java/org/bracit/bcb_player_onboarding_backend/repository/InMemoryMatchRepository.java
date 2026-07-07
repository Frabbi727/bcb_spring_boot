package org.bracit.bcb_player_onboarding_backend.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.bracit.bcb_player_onboarding_backend.domain.Match;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMatchRepository implements MatchRepository {

    private final Map<String, Match> database = new ConcurrentHashMap<>();

    public InMemoryMatchRepository() {
        // Seed default matches
        save(Match.builder()
                .title("U14 District Championship Opener")
                .category("U14")
                .venue("Mirpur Outer Stadium")
                .dateTime(LocalDateTime.now().plusDays(5))
                .opponent("Dhaka Academy")
                .build());
        save(Match.builder()
                .title("U16 Divisional Selection Match")
                .category("U16")
                .venue("Savar Zilla Ground")
                .dateTime(LocalDateTime.now().plusDays(10))
                .opponent("Rajshahi Tigers")
                .build());
    }

    @Override
    public Match save(Match match) {
        if (match.getId() == null || match.getId().trim().isEmpty()) {
            match.setId("MCH-" + (database.size() + 101));
        }
        database.put(match.getId(), match);
        return match;
    }

    @Override
    public Optional<Match> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Match> findByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return findAll();
        }
        String cat = category.toUpperCase().trim();
        return database.values().stream()
                .filter(m -> m.getCategory() != null && m.getCategory().toUpperCase().equals(cat))
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void deleteById(String id) {
        if (id != null) {
            database.remove(id);
        }
    }
}
