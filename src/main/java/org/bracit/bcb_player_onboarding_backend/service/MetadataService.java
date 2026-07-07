package org.bracit.bcb_player_onboarding_backend.service;

import java.util.List;

import org.bracit.bcb_player_onboarding_backend.common.exception.BusinessException;
import org.bracit.bcb_player_onboarding_backend.common.exception.ResourceNotFoundException;
import org.bracit.bcb_player_onboarding_backend.domain.MetadataModels.*;
import org.bracit.bcb_player_onboarding_backend.repository.MetadataRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataService {

    private final MetadataRepository metadataRepository;

    // Playing Roles
    public List<PlayingRole> getPlayingRoles() {
        return metadataRepository.getPlayingRoles();
    }
    public PlayingRole createPlayingRole(PlayingRole role) {
        return metadataRepository.savePlayingRole(role);
    }
    public PlayingRole updatePlayingRole(String code, PlayingRole role) {
        PlayingRole existing = metadataRepository.findPlayingRoleByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Playing role not found with code: " + code));
        existing.setNameEn(role.getNameEn());
        existing.setNameBn(role.getNameBn());
        return metadataRepository.savePlayingRole(existing);
    }
    public void deletePlayingRole(String code) {
        metadataRepository.deletePlayingRole(code);
    }

    // Batting Positions
    public List<BattingPosition> getBattingPositions() {
        return metadataRepository.getBattingPositions();
    }
    public BattingPosition createBattingPosition(BattingPosition pos) {
        return metadataRepository.saveBattingPosition(pos);
    }
    public BattingPosition updateBattingPosition(String code, BattingPosition pos) {
        BattingPosition existing = metadataRepository.findBattingPositionByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Batting position not found with code: " + code));
        existing.setNameEn(pos.getNameEn());
        existing.setNameBn(pos.getNameBn());
        return metadataRepository.saveBattingPosition(existing);
    }
    public void deleteBattingPosition(String code) {
        metadataRepository.deleteBattingPosition(code);
    }

    // Bowling Styles
    public List<BowlingStyle> getBowlingStyles() {
        return metadataRepository.getBowlingStyles();
    }
    public BowlingStyle createBowlingStyle(BowlingStyle style) {
        return metadataRepository.saveBowlingStyle(style);
    }
    public BowlingStyle updateBowlingStyle(String code, BowlingStyle style) {
        BowlingStyle existing = metadataRepository.findBowlingStyleByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Bowling style not found with code: " + code));
        existing.setNameEn(style.getNameEn());
        existing.setNameBn(style.getNameBn());
        return metadataRepository.saveBowlingStyle(existing);
    }
    public void deleteBowlingStyle(String code) {
        metadataRepository.deleteBowlingStyle(code);
    }

    // Competitive Levels
    public List<CompetitiveLevel> getCompetitiveLevels() {
        return metadataRepository.getCompetitiveLevels();
    }
    public CompetitiveLevel createCompetitiveLevel(CompetitiveLevel level) {
        return metadataRepository.saveCompetitiveLevel(level);
    }
    public CompetitiveLevel updateCompetitiveLevel(String code, CompetitiveLevel level) {
        CompetitiveLevel existing = metadataRepository.findCompetitiveLevelByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Competitive level not found with code: " + code));
        existing.setNameEn(level.getNameEn());
        existing.setNameBn(level.getNameBn());
        return metadataRepository.saveCompetitiveLevel(existing);
    }
    public void deleteCompetitiveLevel(String code) {
        metadataRepository.deleteCompetitiveLevel(code);
    }

    // Education Statuses
    public List<EducationStatus> getEducationStatuses() {
        return metadataRepository.getEducationStatuses();
    }
    public EducationStatus createEducationStatus(EducationStatus status) {
        return metadataRepository.saveEducationStatus(status);
    }
    public EducationStatus updateEducationStatus(String code, EducationStatus status) {
        EducationStatus existing = metadataRepository.findEducationStatusByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Education status not found with code: " + code));
        existing.setNameEn(status.getNameEn());
        existing.setNameBn(status.getNameBn());
        return metadataRepository.saveEducationStatus(existing);
    }
    public void deleteEducationStatus(String code) {
        metadataRepository.deleteEducationStatus(code);
    }

    // Schools
    public List<School> getSchools(String search) {
        return metadataRepository.getSchools(search);
    }
    public School createSchool(School school) {
        return metadataRepository.saveSchool(school);
    }
    public School updateSchool(String id, School school) {
        School existing = metadataRepository.findSchoolById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with ID: " + id));
        existing.setNameEn(school.getNameEn());
        existing.setNameBn(school.getNameBn());
        return metadataRepository.saveSchool(existing);
    }
    public void deleteSchool(String id) {
        metadataRepository.deleteSchool(id);
    }

    // Location Hierarchy
    public List<Division> getLocations() {
        return metadataRepository.getLocations();
    }
    public Division createDivision(Division division) {
        return metadataRepository.saveDivision(division);
    }
    public District createDistrict(String divisionId, District district) {
        return metadataRepository.saveDistrict(divisionId, district);
    }
    public Upazila createUpazila(String districtId, Upazila upazila) {
        return metadataRepository.saveUpazila(districtId, upazila);
    }
    public Union createUnion(String upazilaId, Union union) {
        return metadataRepository.saveUnion(upazilaId, union);
    }

    public void updateLocation(String nodeType, String id, String nameEn, String nameBn) {
        if ("divisions".equalsIgnoreCase(nodeType)) {
            Division node = metadataRepository.findDivisionById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Division not found: " + id));
            node.setNameEn(nameEn);
            node.setNameBn(nameBn);
            metadataRepository.saveDivision(node);
        } else if ("districts".equalsIgnoreCase(nodeType)) {
            District node = metadataRepository.findDistrictById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("District not found: " + id));
            node.setNameEn(nameEn);
            node.setNameBn(nameBn);
            // Search and update district
            metadataRepository.getLocations().forEach(div -> {
                div.getDistricts().forEach(d -> {
                    if (d.getId().equals(id)) {
                        d.setNameEn(nameEn);
                        d.setNameBn(nameBn);
                    }
                });
            });
        } else if ("upazilas".equalsIgnoreCase(nodeType)) {
            Upazila node = metadataRepository.findUpazilaById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Upazila not found: " + id));
            node.setNameEn(nameEn);
            node.setNameBn(nameBn);
            metadataRepository.getLocations().forEach(div -> {
                div.getDistricts().forEach(dist -> {
                    dist.getUpazilas().forEach(u -> {
                        if (u.getId().equals(id)) {
                            u.setNameEn(nameEn);
                            u.setNameBn(nameBn);
                        }
                    });
                });
            });
        } else if ("unions".equalsIgnoreCase(nodeType)) {
            Union node = metadataRepository.findUnionById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Union not found: " + id));
            node.setNameEn(nameEn);
            node.setNameBn(nameBn);
            metadataRepository.getLocations().forEach(div -> {
                div.getDistricts().forEach(dist -> {
                    dist.getUpazilas().forEach(upa -> {
                        upa.getUnions().forEach(uni -> {
                            if (uni.getId().equals(id)) {
                                uni.setNameEn(nameEn);
                                uni.setNameBn(nameBn);
                            }
                        });
                    });
                });
            });
        } else {
            throw new BusinessException("Invalid location node type: " + nodeType);
        }
    }

    public void deleteLocation(String nodeType, String id) {
        if ("divisions".equalsIgnoreCase(nodeType)) {
            metadataRepository.deleteDivision(id);
        } else if ("districts".equalsIgnoreCase(nodeType)) {
            metadataRepository.deleteDistrict(id);
        } else if ("upazilas".equalsIgnoreCase(nodeType)) {
            metadataRepository.deleteUpazila(id);
        } else if ("unions".equalsIgnoreCase(nodeType)) {
            metadataRepository.deleteUnion(id);
        } else {
            throw new BusinessException("Invalid location node type: " + nodeType);
        }
    }
}
