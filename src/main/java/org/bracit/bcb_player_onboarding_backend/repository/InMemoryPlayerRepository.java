package org.bracit.bcb_player_onboarding_backend.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bracit.bcb_player_onboarding_backend.domain.Player;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerPosition;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerStatus;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPlayerRepository implements PlayerRepository {

    private final Map<String, Player> database = new ConcurrentHashMap<>();

    @Override
    public Player save(Player player) {
        if (player.getId() == null || player.getId().trim().isEmpty()) {
            player.setId("PLR-" + (database.size() + 1));
        }
        database.put(player.getId(), player);
        return player;
    }

    @Override
    public Optional<Player> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Player> findAll(int page, int size, String name, PlayerPosition position, PlayerStatus status) {
        return getFilteredStream(name, position, status)
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public long count(String name, PlayerPosition position, PlayerStatus status) {
        return getFilteredStream(name, position, status).count();
    }

    @Override
    public void deleteById(String id) {
        if (id != null) {
            database.remove(id);
        }
    }

    private Stream<Player> getFilteredStream(String name, PlayerPosition position, PlayerStatus status) {
        Stream<Player> stream = database.values().stream();
        
        if (name != null && !name.trim().isEmpty()) {
            String lowerName = name.toLowerCase().trim();
            stream = stream.filter(p -> p.getName().toLowerCase().contains(lowerName));
        }
        
        if (position != null) {
            stream = stream.filter(p -> p.getPosition() == position);
        }
        
        if (status != null) {
            stream = stream.filter(p -> p.getStatus() == status);
        }
        
        return stream;
    }
}
