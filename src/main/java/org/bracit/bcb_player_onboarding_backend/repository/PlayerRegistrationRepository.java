package org.bracit.bcb_player_onboarding_backend.repository;

import java.util.List;
import java.util.Optional;

import org.bracit.bcb_player_onboarding_backend.domain.PlayerRegistration;

public interface PlayerRegistrationRepository {
    PlayerRegistration save(PlayerRegistration registration);
    Optional<PlayerRegistration> findById(String id);
    Optional<PlayerRegistration> findLatestByPhoneNumber(String phoneNumber);
    List<PlayerRegistration> findByPhoneNumber(String phoneNumber);
    List<PlayerRegistration> findAll(int page, int size, String status, String name, String districtId, String divisionId, String search);
    long count(String status, String name, String districtId, String divisionId, String search);
    void delete(PlayerRegistration registration);
}
