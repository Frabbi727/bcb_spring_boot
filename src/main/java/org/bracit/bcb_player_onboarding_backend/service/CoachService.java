package org.bracit.bcb_player_onboarding_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;
import org.bracit.bcb_player_onboarding_backend.common.exception.ResourceNotFoundException;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerRegistration;
import org.bracit.bcb_player_onboarding_backend.dto.auth.AuthResponse.UserInfo;
import org.bracit.bcb_player_onboarding_backend.repository.PlayerRegistrationRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoachService {

    private final PlayerRegistrationRepository registrationRepository;

    public List<PlayerRegistration> getRegistrationsForCoach(String districtId, String status, String search) {
        log.info("Coach fetching registrations for district: {}, status: {}, search: {}", districtId, status, search);
        // Page index 0, size 100 for coach dashboard listing
        return registrationRepository.findAll(0, 100, status, null, districtId, null, search);
    }

    public PlayerRegistration getRegistrationById(String id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with ID: " + id));
    }

    public PlayerRegistration verifyRegistration(String id, List<String> expertise, String comment, UserInfo coach) {
        log.info("Coach verifying registration ID: {}", id);
        PlayerRegistration registration = getRegistrationById(id);
        
        if (!"pending_approval".equalsIgnoreCase(registration.getStatus())) {
            throw new BusinessException("Only pending registrations can be verified by a coach.");
        }

        registration.setCacheVerified(true);
        registration.setPlayerExpertise(expertise);
        registration.setCoachComment(comment);
        registration.setVerifiedByCoachId(coach.getId());
        registration.setVerifiedByCoachName(coach.getName());
        
        return registrationRepository.save(registration);
    }

    public PlayerRegistration rejectRegistration(String id, List<String> feedbackChips, String comment, LocalDateTime resubmitAfterDate, boolean isPermanent) {
        log.info("Coach rejecting registration ID: {}", id);
        PlayerRegistration registration = getRegistrationById(id);
        
        if (!"pending_approval".equalsIgnoreCase(registration.getStatus())) {
            throw new BusinessException("Only pending registrations can be rejected.");
        }

        registration.setStatus("rejected");
        registration.setFeedbackChips(feedbackChips);
        registration.setRejectionComment(comment);
        registration.setResubmitAfterDate(resubmitAfterDate);
        registration.setPermanent(isPermanent);
        
        return registrationRepository.save(registration);
    }
}
