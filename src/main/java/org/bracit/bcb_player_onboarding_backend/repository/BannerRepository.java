package org.bracit.bcb_player_onboarding_backend.repository;

import java.util.List;
import java.util.Optional;

import org.bracit.bcb_player_onboarding_backend.domain.SponsorBanner;

public interface BannerRepository {
    SponsorBanner save(SponsorBanner banner);
    Optional<SponsorBanner> findById(String id);
    List<SponsorBanner> findAll();
    void deleteById(String id);
}
