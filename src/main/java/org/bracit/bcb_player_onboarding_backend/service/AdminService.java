package org.bracit.bcb_player_onboarding_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.bracit.bcb_player_onboarding_backend.common.dto.PagedResponse;
import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;
import org.bracit.bcb_player_onboarding_backend.common.exception.ResourceNotFoundException;
import org.bracit.bcb_player_onboarding_backend.domain.Match;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerRegistration;
import org.bracit.bcb_player_onboarding_backend.domain.SponsorBanner;
import org.bracit.bcb_player_onboarding_backend.dto.auth.AuthResponse.UserInfo;
import org.bracit.bcb_player_onboarding_backend.repository.BannerRepository;
import org.bracit.bcb_player_onboarding_backend.repository.MatchRepository;
import org.bracit.bcb_player_onboarding_backend.repository.PlayerRegistrationRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final PlayerRegistrationRepository registrationRepository;
    private final MatchRepository matchRepository;
    private final BannerRepository bannerRepository;

    public PagedResponse<PlayerRegistration> getRegistrationsForAdmin(int page, int size, String status, String districtId, String divisionId, String search) {
        log.info("Admin listing registrations page: {}, size: {}, status: {}, district: {}, division: {}, search: {}", 
                page, size, status, districtId, divisionId, search);
        List<PlayerRegistration> registrations = registrationRepository.findAll(page, size, status, null, districtId, divisionId, search);
        long count = registrationRepository.count(status, null, districtId, divisionId, search);
        return PagedResponse.of(registrations, page, size, count);
    }

    public PlayerRegistration getRegistrationById(String id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with ID: " + id));
    }

    public PlayerRegistration approveRegistration(String id, String comment, UserInfo admin) {
        log.info("Admin approving registration ID: {}", id);
        PlayerRegistration registration = getRegistrationById(id);

        if (!"pending_approval".equalsIgnoreCase(registration.getStatus())) {
            throw new BusinessException("Only pending registrations can be approved.");
        }

        // Generate serial ID: e.g. U14-2026-0043
        String category = "U14";
        if (registration.getPersonalDetails() != null && registration.getPersonalDetails().getDob() != null) {
            int age = LocalDateTime.now().getYear() - registration.getPersonalDetails().getDob().getYear();
            if (age >= 16 && age < 19) category = "U16";
            else if (age >= 19) category = "U19";
        }
        
        String year = String.valueOf(LocalDateTime.now().getYear());
        String serial = String.format("%04d", (int) (Math.random() * 9000) + 1000);
        String playerId = category + "-" + year + "-" + serial;

        registration.setStatus("approved");
        registration.setPlayerId(playerId);
        registration.setQrCodeData("https://bcb.gov.bd/player/" + playerId);
        registration.setAdminComment(comment);
        registration.setApprovedBy(admin.getName());
        registration.setApprovedAt(LocalDateTime.now());

        return registrationRepository.save(registration);
    }

    public PlayerRegistration rejectRegistration(String id, List<String> feedbackChips, String comment, boolean isPermanent) {
        log.info("Admin rejecting registration ID: {}", id);
        PlayerRegistration registration = getRegistrationById(id);

        if (!"pending_approval".equalsIgnoreCase(registration.getStatus())) {
            throw new BusinessException("Only pending registrations can be rejected.");
        }

        registration.setStatus("rejected");
        registration.setFeedbackChips(feedbackChips);
        registration.setRejectionComment(comment);
        registration.setPermanent(isPermanent);

        return registrationRepository.save(registration);
    }

    // Match Calendar Management CRUD
    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    public Match updateMatch(String id, Match match) {
        Match existing = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with ID: " + id));
        existing.setTitle(match.getTitle());
        existing.setCategory(match.getCategory());
        existing.setVenue(match.getVenue());
        existing.setDateTime(match.getDateTime());
        existing.setOpponent(match.getOpponent());
        return matchRepository.save(existing);
    }

    public void deleteMatch(String id) {
        matchRepository.deleteById(id);
    }

    // Sponsor Banners Management CRUD
    public List<SponsorBanner> getBanners() {
        return bannerRepository.findAll();
    }

    public SponsorBanner createBanner(SponsorBanner banner) {
        return bannerRepository.save(banner);
    }

    public SponsorBanner updateBanner(String id, SponsorBanner banner) {
        SponsorBanner existing = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner not found with ID: " + id));
        existing.setTitle(banner.getTitle());
        existing.setImageUrl(banner.getImageUrl());
        existing.setTargetUrl(banner.getTargetUrl());
        return bannerRepository.save(existing);
    }

    public void deleteBanner(String id) {
        bannerRepository.deleteById(id);
    }
}
