package org.bracit.bcb_player_onboarding_backend.mcp;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bracit.bcb_player_onboarding_backend.domain.PersonalDetails;
import org.bracit.bcb_player_onboarding_backend.domain.PlayerRegistration;
import org.bracit.bcb_player_onboarding_backend.service.PlayerRegistrationService;
import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerOnboardingMcpTools {

    private final PlayerRegistrationService playerRegistrationService;

    @McpTool(name = "verifyPlayerEligibility", description = "Verifies if a player is eligible for onboarding based on age and health status")
    public String verifyPlayerEligibility(
            @McpToolParam(description = "Full name of the player", required = true) String name,
            @McpToolParam(description = "Age of the player in years", required = true) int age,
            @McpToolParam(description = "Whether the player passed the medical fitness check", required = true) boolean medicalFitnessPassed) {
        
        log.info("MCP Tool: Verifying eligibility for player: {} (Age: {})", name, age);
        
        if (age < 18 || age > 40) {
            return "Player " + name + " is ineligible: Age must be between 18 and 40.";
        }
        if (!medicalFitnessPassed) {
            return "Player " + name + " is ineligible: Medical fitness check has not been passed.";
        }
        return "Player " + name + " is fully eligible for onboarding.";
    }

    @McpTool(name = "registerPlayer", description = "Registers a new player in the onboarding database")
    public String registerPlayer(
            @McpToolParam(description = "Full name of the player", required = true) String name,
            @McpToolParam(description = "Player's position or role (e.g. Batsman, Bowler, Wicketkeeper, All-rounder)", required = true) String position,
            @McpToolParam(description = "Whether the player passed the medical fitness check", required = true) boolean medicalFitnessPassed,
            @McpToolParam(description = "Age of the player in years", required = true) int age,
            @McpToolParam(description = "Verified phone number of the player", required = true) String phoneNumber) {
        
        log.info("MCP Tool: Registering player: {} as {} (Age: {})", name, position, age);
        
        try {
            // Build draft and update personal details
            PlayerRegistration draft = playerRegistrationService.getOrCreateDraft(phoneNumber);
            playerRegistrationService.savePersonal(phoneNumber, PersonalDetails.builder()
                    .nameEn(name)
                    .dob(LocalDateTime.now().minusYears(age))
                    .build());
            
            return "Player draft registered via MCP: " + name + " (ID: " + draft.getId() + ")";
        } catch (BusinessException e) {
            return "Player registration failed: " + e.getMessage();
        }
    }
}
