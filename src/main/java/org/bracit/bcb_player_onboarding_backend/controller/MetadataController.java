package org.bracit.bcb_player_onboarding_backend.controller;

import java.util.List;

import org.bracit.bcb_player_onboarding_backend.common.dto.ApiResponse;
import org.bracit.bcb_player_onboarding_backend.domain.MetadataModels.*;
import org.bracit.bcb_player_onboarding_backend.service.MetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/metadata")
@RequiredArgsConstructor
public class MetadataController {

    private final MetadataService metadataService;

    @GetMapping("/playing-roles")
    public ResponseEntity<ApiResponse<List<PlayingRole>>> getPlayingRoles() {
        List<PlayingRole> list = metadataService.getPlayingRoles();
        return ResponseEntity.ok(ApiResponse.success(list, "Playing roles retrieved."));
    }

    @GetMapping("/batting-positions")
    public ResponseEntity<ApiResponse<List<BattingPosition>>> getBattingPositions() {
        List<BattingPosition> list = metadataService.getBattingPositions();
        return ResponseEntity.ok(ApiResponse.success(list, "Batting positions retrieved."));
    }

    @GetMapping("/bowling-styles")
    public ResponseEntity<ApiResponse<List<BowlingStyle>>> getBowlingStyles() {
        List<BowlingStyle> list = metadataService.getBowlingStyles();
        return ResponseEntity.ok(ApiResponse.success(list, "Bowling styles retrieved."));
    }

    @GetMapping("/competitive-levels")
    public ResponseEntity<ApiResponse<List<CompetitiveLevel>>> getCompetitiveLevels() {
        List<CompetitiveLevel> list = metadataService.getCompetitiveLevels();
        return ResponseEntity.ok(ApiResponse.success(list, "Competitive levels retrieved."));
    }

    @GetMapping("/education-statuses")
    public ResponseEntity<ApiResponse<List<EducationStatus>>> getEducationStatuses() {
        List<EducationStatus> list = metadataService.getEducationStatuses();
        return ResponseEntity.ok(ApiResponse.success(list, "Education statuses retrieved."));
    }

    @GetMapping("/schools")
    public ResponseEntity<ApiResponse<List<School>>> getSchools(@RequestParam(required = false) String search) {
        List<School> list = metadataService.getSchools(search);
        return ResponseEntity.ok(ApiResponse.success(list, "Schools list retrieved."));
    }

    @GetMapping("/locations")
    public ResponseEntity<ApiResponse<List<Division>>> getLocations() {
        List<Division> list = metadataService.getLocations();
        return ResponseEntity.ok(ApiResponse.success(list, "Location dynamic hierarchy nodes retrieved."));
    }
}
