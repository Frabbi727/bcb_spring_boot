package org.bracit.bcb_player_onboarding_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.bracit.bcb_player_onboarding_backend.common.dto.PagedResponse;
import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;
import org.bracit.bcb_player_onboarding_backend.common.exception.ResourceNotFoundException;
import org.bracit.bcb_player_onboarding_backend.domain.Player;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerPosition;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerStatus;
import org.bracit.bcb_player_onboarding_backend.dto.PlayerRegisterRequest;
import org.bracit.bcb_player_onboarding_backend.dto.PlayerResponse;
import org.bracit.bcb_player_onboarding_backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public PlayerResponse registerPlayer(PlayerRegisterRequest request) {
        log.info("Processing registration for player: {}", request.getName());

        // Validate age constraint (Double check in service layer)
        if (request.getAge() < 18 || request.getAge() > 40) {
            throw new BusinessException("Player age must be between 18 and 40 years.");
        }

        // Validate position enum mapping
        PlayerPosition position = PlayerPosition.fromString(request.getPosition());
        if (position == null) {
            throw new BusinessException("Invalid position: " + request.getPosition() 
                    + ". Valid positions: BATSMAN, BOWLER, WICKETKEEPER, ALL_ROUNDER");
        }

        // Determine initial status based on medical fitness
        PlayerStatus status = Boolean.TRUE.equals(request.getMedicalFitnessPassed()) 
                ? PlayerStatus.ELIGIBLE 
                : PlayerStatus.PENDING_MEDICAL;

        Player player = Player.builder()
                .name(request.getName().trim())
                .age(request.getAge())
                .position(position)
                .medicalFitnessPassed(Boolean.TRUE.equals(request.getMedicalFitnessPassed()))
                .status(status)
                .build();

        Player savedPlayer = playerRepository.save(player);
        log.info("Player successfully registered with ID: {}", savedPlayer.getId());
        
        return PlayerResponse.fromDomain(savedPlayer);
    }

    @Override
    public PlayerResponse getPlayerById(String id) {
        log.info("Fetching player by ID: {}", id);
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + id));
        return PlayerResponse.fromDomain(player);
    }

    @Override
    public PagedResponse<PlayerResponse> searchPlayers(int page, int size, String name, String positionStr, String statusStr) {
        log.info("Searching players - page: {}, size: {}, name: {}, position: {}, status: {}", 
                page, size, name, positionStr, statusStr);

        PlayerPosition position = null;
        if (positionStr != null && !positionStr.trim().isEmpty()) {
            position = PlayerPosition.fromString(positionStr);
            if (position == null) {
                throw new BusinessException("Invalid position filter: " + positionStr);
            }
        }

        PlayerStatus status = null;
        if (statusStr != null && !statusStr.trim().isEmpty()) {
            try {
                status = PlayerStatus.valueOf(statusStr.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("Invalid status filter: " + statusStr);
            }
        }

        List<Player> players = playerRepository.findAll(page, size, name, position, status);
        long count = playerRepository.count(name, position, status);

        List<PlayerResponse> content = players.stream()
                .map(PlayerResponse::fromDomain)
                .collect(Collectors.toList());

        return PagedResponse.of(content, page, size, count);
    }

    @Override
    public PlayerResponse verifyEligibility(String id) {
        log.info("Re-evaluating eligibility status for player ID: {}", id);
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + id));

        // Evaluate eligibility based on core business rules
        boolean isAgeEligible = player.getAge() >= 18 && player.getAge() <= 40;
        boolean isMedicalEligible = player.isMedicalFitnessPassed();

        if (isAgeEligible && isMedicalEligible) {
            player.setStatus(PlayerStatus.ELIGIBLE);
        } else {
            player.setStatus(PlayerStatus.INELIGIBLE);
        }

        Player updatedPlayer = playerRepository.save(player);
        log.info("Eligibility evaluated for player: {}. Result status: {}", player.getName(), player.getStatus());
        
        return PlayerResponse.fromDomain(updatedPlayer);
    }

    @Override
    public PlayerResponse updateMedicalStatus(String id, boolean passed) {
        log.info("Updating medical fitness status for player ID: {} to: {}", id, passed);
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + id));

        player.setMedicalFitnessPassed(passed);
        
        // Re-evaluate eligibility automatically
        boolean isAgeEligible = player.getAge() >= 18 && player.getAge() <= 40;
        if (isAgeEligible && passed) {
            player.setStatus(PlayerStatus.ELIGIBLE);
        } else if (!passed) {
            player.setStatus(PlayerStatus.PENDING_MEDICAL);
        } else {
            player.setStatus(PlayerStatus.INELIGIBLE);
        }

        Player updatedPlayer = playerRepository.save(player);
        log.info("Player updated: {}", updatedPlayer);
        
        return PlayerResponse.fromDomain(updatedPlayer);
    }
}
