package org.bracit.bcb_player_onboarding_backend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.bracit.bcb_player_onboarding_backend.domain.SponsorBanner;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBannerRepository implements BannerRepository {

    private final Map<String, SponsorBanner> database = new ConcurrentHashMap<>();

    public InMemoryBannerRepository() {
        // Seed default sponsor banners
        save(SponsorBanner.builder()
                .title("Robic Cricket Sponsor Banner")
                .imageUrl("https://storage.bcb.gov.bd/banners/robi_banner.jpg")
                .targetUrl("https://robi.com.bd")
                .build());
        save(SponsorBanner.builder()
                .title("GP Youth Onboarding Drive")
                .imageUrl("https://storage.bcb.gov.bd/banners/gp_banner.jpg")
                .targetUrl("https://grameenphone.com")
                .build());
    }

    @Override
    public SponsorBanner save(SponsorBanner banner) {
        if (banner.getId() == null || banner.getId().trim().isEmpty()) {
            banner.setId("BAN-" + (database.size() + 1001));
        }
        database.put(banner.getId(), banner);
        return banner;
    }

    @Override
    public Optional<SponsorBanner> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<SponsorBanner> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void deleteById(String id) {
        if (id != null) {
            database.remove(id);
        }
    }
}
