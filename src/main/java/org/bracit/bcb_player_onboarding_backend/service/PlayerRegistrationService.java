package org.bracit.bcb_player_onboarding_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;
import org.bracit.bcb_player_onboarding_backend.domain.*;
import org.bracit.bcb_player_onboarding_backend.domain.MetadataModels.School;
import org.bracit.bcb_player_onboarding_backend.repository.MatchRepository;
import org.bracit.bcb_player_onboarding_backend.repository.PlayerRegistrationRepository;
import org.bracit.bcb_player_onboarding_backend.repository.MetadataRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerRegistrationService {

    private final PlayerRegistrationRepository registrationRepository;
    private final MatchRepository matchRepository;
    private final MetadataRepository metadataRepository;

    public PlayerRegistration getOrCreateDraft(String phoneNumber) {
        return registrationRepository.findLatestByPhoneNumber(phoneNumber)
                .orElseGet(() -> {
                    PlayerRegistration newDraft = PlayerRegistration.builder()
                            .phoneNumber(phoneNumber)
                            .status("draft")
                            .createdAt(LocalDateTime.now())
                            .build();
                    return registrationRepository.save(newDraft);
                });
    }

    public PlayerRegistration savePersonal(String phoneNumber, PersonalDetails details) {
        log.info("Saving personal details for player phone: {}", phoneNumber);
        
        // Validation: Verify age boundaries if date of birth is submitted
        if (details.getDob() != null) {
            int age = LocalDateTime.now().getYear() - details.getDob().getYear();
            if (age < 18 || age > 40) {
                throw new BusinessException("Player eligibility constraint: age must be between 18 and 40 years.");
            }
        }

        PlayerRegistration draft = getOrCreateDraft(phoneNumber);
        draft.setPersonalDetails(details);
        return registrationRepository.save(draft);
    }

    public PlayerRegistration saveCricket(String phoneNumber, CricketExperience experience) {
        log.info("Saving cricket experience for player phone: {}", phoneNumber);
        
        // Verify codes are valid
        if (experience.getPrimaryRoleCode() != null && 
                metadataRepository.findPlayingRoleByCode(experience.getPrimaryRoleCode()).isEmpty()) {
            throw new BusinessException("Invalid playing role code: " + experience.getPrimaryRoleCode());
        }
        if (experience.getPreferredBattingPositionCode() != null && 
                metadataRepository.findBattingPositionByCode(experience.getPreferredBattingPositionCode()).isEmpty()) {
            throw new BusinessException("Invalid batting position code: " + experience.getPreferredBattingPositionCode());
        }
        if (experience.getPrimaryBowlingStyleCode() != null && 
                metadataRepository.findBowlingStyleByCode(experience.getPrimaryBowlingStyleCode()).isEmpty()) {
            throw new BusinessException("Invalid bowling style code: " + experience.getPrimaryBowlingStyleCode());
        }
        if (experience.getHighestCompetitiveLevelCode() != null && 
                metadataRepository.findCompetitiveLevelByCode(experience.getHighestCompetitiveLevelCode()).isEmpty()) {
            throw new BusinessException("Invalid competitive level code: " + experience.getHighestCompetitiveLevelCode());
        }

        PlayerRegistration draft = getOrCreateDraft(phoneNumber);
        draft.setCricketExperience(experience);
        return registrationRepository.save(draft);
    }

    public PlayerRegistration saveEducation(String phoneNumber, EducationDetails details) {
        log.info("Saving education details for player phone: {}", phoneNumber);

        if (details.getSchoolCollegeId() != null && 
                metadataRepository.findSchoolById(details.getSchoolCollegeId()).isEmpty()) {
            throw new BusinessException("Invalid school/college ID: " + details.getSchoolCollegeId());
        }

        PlayerRegistration draft = getOrCreateDraft(phoneNumber);
        draft.setEducationDetails(details);
        return registrationRepository.save(draft);
    }

    public PlayerRegistration saveGuardian(String phoneNumber, GuardianDetails details) {
        log.info("Saving guardian details for player phone: {}", phoneNumber);
        PlayerRegistration draft = getOrCreateDraft(phoneNumber);
        draft.setGuardianDetails(details);
        return registrationRepository.save(draft);
    }

    public PlayerRegistration saveLocation(String phoneNumber, LocationDetails details) {
        log.info("Saving address locations for player phone: {}", phoneNumber);

        // Optional Validation: Division/District verify
        if (details.getPresentAddressObj() != null) {
            String districtId = details.getPresentAddressObj().getDistrictId();
            if (districtId != null && metadataRepository.findDistrictById(districtId).isEmpty()) {
                throw new BusinessException("Invalid district ID: " + districtId);
            }
        }

        PlayerRegistration draft = getOrCreateDraft(phoneNumber);
        draft.setLocationDetails(details);
        return registrationRepository.save(draft);
    }

    public PlayerRegistration saveDocuments(String phoneNumber, DocumentDetails details) {
        log.info("Saving uploaded document locations for player phone: {}", phoneNumber);
        PlayerRegistration draft = getOrCreateDraft(phoneNumber);
        draft.setDocumentDetails(details);
        return registrationRepository.save(draft);
    }

    public PlayerRegistration submitRegistration(String phoneNumber) {
        log.info("Submitting registration review for phone: {}", phoneNumber);
        PlayerRegistration draft = registrationRepository.findLatestByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new BusinessException("No active registration draft found to submit."));

        if (!"draft".equalsIgnoreCase(draft.getStatus())) {
            throw new BusinessException("Registration already submitted. Current status: " + draft.getStatus());
        }

        // Validate draft completeness
        if (draft.getPersonalDetails() == null || draft.getPersonalDetails().getNameEn() == null) {
            throw new BusinessException("Incomplete registration: Personal information is required.");
        }
        if (draft.getCricketExperience() == null || draft.getCricketExperience().getPrimaryRoleCode() == null) {
            throw new BusinessException("Incomplete registration: Cricket experience is required.");
        }
        if (draft.getLocationDetails() == null || draft.getLocationDetails().getPresentAddressObj() == null) {
            throw new BusinessException("Incomplete registration: Address location details are required.");
        }
        if (draft.getDocumentDetails() == null || draft.getDocumentDetails().getBirthCertificatePath() == null) {
            throw new BusinessException("Incomplete registration: Birth certificate document is required.");
        }

        draft.setStatus("pending_approval");
        draft.setCreatedAt(LocalDateTime.now());
        return registrationRepository.save(draft);
    }

    public void discardDraft(String phoneNumber) {
        log.info("Discarding draft registration for phone: {}", phoneNumber);
        PlayerRegistration draft = registrationRepository.findLatestByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new BusinessException("No active registration draft found to discard."));
        
        if (!"draft".equalsIgnoreCase(draft.getStatus())) {
            throw new BusinessException("Cannot discard a submitted registration.");
        }
        registrationRepository.delete(draft);
    }

    public List<PlayerRegistration> getRegistrationHistory(String phoneNumber) {
        return registrationRepository.findByPhoneNumber(phoneNumber);
    }

    public String getLatestRegistrationStatus(String phoneNumber) {
        return registrationRepository.findLatestByPhoneNumber(phoneNumber)
                .map(PlayerRegistration::getStatus)
                .orElse("none");
    }

    public List<Match> getMatchesByCategory(String category) {
        return matchRepository.findByCategory(category);
    }
}
