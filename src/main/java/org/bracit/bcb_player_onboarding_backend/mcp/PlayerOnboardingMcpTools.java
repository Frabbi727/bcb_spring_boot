package org.bracit.bcb_player_onboarding_backend.mcp;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bracit.bcb_player_onboarding_backend.dto.PlayerRegisterRequest;
import org.bracit.bcb_player_onboarding_backend.dto.PlayerResponse;
import org.bracit.bcb_player_onboarding_backend.service.PlayerService;
import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerOnboardingMcpTools {

    private final PlayerService playerService;

    @McpTool(name = "verifyPlayerEligibility", description = "Verifies if a player is eligible for onboarding based on age and health status")
    public String verifyPlayerEligibility(
            @McpToolParam(description = "Full name of the player", required = true) String name,
            @McpToolParam(description = "Age of the player in years", required = true) int age,
            @McpToolParam(description = "Whether the player passed the medical fitness check", required = true) boolean medicalFitnessPassed) {
        
        log.info("MCP Tool: Verifying eligibility for player: {} (Age: {})", name, age);
        
        try {
            // Register player as a draft/pending with default role to run unified validation
            PlayerResponse player = playerService.registerPlayer(PlayerRegisterRequest.builder()
                    .name(name)
                    .age(age)
                    .position("ALL_ROUNDER")
                    .medicalFitnessPassed(medicalFitnessPassed)
                    .build());
            
            return "Player verification result: " + player.getName() + " is registered with ID: " 
                    + player.getId() + " and status: " + player.getStatus();
        } catch (BusinessException e) {
            return "Player verification failed: " + e.getMessage();
        }
    }

    @McpTool(name = "registerPlayer", description = "Registers a new player in the onboarding database")
    public String registerPlayer(
            @McpToolParam(description = "Full name of the player", required = true) String name,
            @McpToolParam(description = "Player's position or role (e.g. Batsman, Bowler, Wicketkeeper, All-rounder)", required = true) String position,
            @McpToolParam(description = "Whether the player passed the medical fitness check", required = true) boolean medicalFitnessPassed,
            @McpToolParam(description = "Age of the player in years", required = true) int age) {
        
        log.info("MCP Tool: Registering player: {} as {} (Age: {})", name, position, age);
        
        try {
            PlayerResponse player = playerService.registerPlayer(PlayerRegisterRequest.builder()
                    .name(name)
                    .age(age)
                    .position(position)
                    .medicalFitnessPassed(medicalFitnessPassed)
                    .build());
            return "Player successfully registered via MCP: " + player.getName() + " (ID: " + player.getId() 
                    + ", Status: " + player.getStatus() + ", Position: " + player.getPosition() + ")";
        } catch (BusinessException e) {
            return "Player registration failed: " + e.getMessage();
        }
    }
}
