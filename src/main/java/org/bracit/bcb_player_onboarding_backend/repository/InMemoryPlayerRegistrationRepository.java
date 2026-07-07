package org.bracit.bcb_player_onboarding_backend.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bracit.bcb_player_onboarding_backend.domain.PlayerRegistration;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPlayerRegistrationRepository implements PlayerRegistrationRepository {

    private final Map<String, PlayerRegistration> database = new ConcurrentHashMap<>();

    @Override
    public PlayerRegistration save(PlayerRegistration registration) {
        if (registration.getId() == null || registration.getId().trim().isEmpty()) {
            registration.setId("REG-" + (database.size() + 1001));
        }
        database.put(registration.getId(), registration);
        return registration;
    }

    @Override
    public Optional<PlayerRegistration> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Optional<PlayerRegistration> findLatestByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return Optional.empty();
        return database.values().stream()
                .filter(r -> phoneNumber.equals(r.getPhoneNumber()))
                .max(Comparator.comparing(PlayerRegistration::getCreatedAt, Comparator.nullsFirst(Comparator.naturalOrder())));
    }

    @Override
    public List<PlayerRegistration> findByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return List.of();
        return database.values().stream()
                .filter(r -> phoneNumber.equals(r.getPhoneNumber()))
                .sorted(Comparator.comparing(PlayerRegistration::getCreatedAt, Comparator.nullsFirst(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerRegistration> findAll(int page, int size, String status, String name, String districtId, String divisionId, String search) {
        return getFilteredStream(status, name, districtId, divisionId, search)
                .sorted(Comparator.comparing(PlayerRegistration::getCreatedAt, Comparator.nullsFirst(Comparator.reverseOrder())))
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public long count(String status, String name, String districtId, String divisionId, String search) {
        return getFilteredStream(status, name, districtId, divisionId, search).count();
    }

    @Override
    public void delete(PlayerRegistration registration) {
        if (registration != null && registration.getId() != null) {
            database.remove(registration.getId());
        }
    }

    private Stream<PlayerRegistration> getFilteredStream(String status, String name, String districtId, String divisionId, String search) {
        Stream<PlayerRegistration> stream = database.values().stream();
        
        if (status != null && !status.trim().isEmpty() && !"all".equalsIgnoreCase(status)) {
            stream = stream.filter(r -> status.equalsIgnoreCase(r.getStatus()));
        }
        
        if (name != null && !name.trim().isEmpty()) {
            String lowerName = name.toLowerCase().trim();
            stream = stream.filter(r -> r.getPersonalDetails() != null && 
                    r.getPersonalDetails().getNameEn() != null &&
                    r.getPersonalDetails().getNameEn().toLowerCase().contains(lowerName));
        }
        
        if (districtId != null && !districtId.trim().isEmpty()) {
            stream = stream.filter(r -> r.getLocationDetails() != null && 
                    r.getLocationDetails().getPresentAddressObj() != null &&
                    districtId.equalsIgnoreCase(r.getLocationDetails().getPresentAddressObj().getDistrictId()));
        }

        if (divisionId != null && !divisionId.trim().isEmpty()) {
            stream = stream.filter(r -> r.getLocationDetails() != null && 
                    r.getLocationDetails().getPresentAddressObj() != null &&
                    divisionId.equalsIgnoreCase(r.getLocationDetails().getPresentAddressObj().getDivisionId()));
        }

        if (search != null && !search.trim().isEmpty()) {
            String q = search.toLowerCase().trim();
            stream = stream.filter(r -> 
                (r.getPhoneNumber() != null && r.getPhoneNumber().contains(q)) ||
                (r.getPersonalDetails() != null && r.getPersonalDetails().getNameEn() != null && r.getPersonalDetails().getNameEn().toLowerCase().contains(q)) ||
                (r.getPlayerId() != null && r.getPlayerId().toLowerCase().contains(q))
            );
        }
        
        return stream;
    }
}
