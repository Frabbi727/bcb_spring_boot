package org.bracit.bcb_player_onboarding_backend.mcp;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PlayerOnboardingMcpTools {

    @McpTool(name = "verifyPlayerEligibility", description = "Verifies if a player is eligible for onboarding based on age and health status")
    public String verifyPlayerEligibility(
            @McpToolParam(description = "Full name of the player", required = true) String name,
            @McpToolParam(description = "Age of the player in years", required = true) int age,
            @McpToolParam(description = "Whether the player passed the medical fitness check", required = true) boolean medicalFitnessPassed) {
        
        log.info("Verifying eligibility for player: {} (Age: {})", name, age);
        
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
            @McpToolParam(description = "Player's position or role (e.g. Batsman, Bowler, Wicketkeeper, All-rounder)", required = true) String position) {
        
        log.info("Registering player: {} as {}", name, position);
        return "Player " + name + " has been successfully registered as a(n) " + position + " with a pending onboarding status.";
    }
}
