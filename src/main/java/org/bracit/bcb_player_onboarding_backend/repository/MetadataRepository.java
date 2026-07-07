package org.bracit.bcb_player_onboarding_backend.repository;

import java.util.List;
import java.util.Optional;

import org.bracit.bcb_player_onboarding_backend.domain.MetadataModels.*;

public interface MetadataRepository {
    // Playing Roles
    List<PlayingRole> getPlayingRoles();
    PlayingRole savePlayingRole(PlayingRole role);
    Optional<PlayingRole> findPlayingRoleByCode(String code);
    void deletePlayingRole(String code);

    // Batting Positions
    List<BattingPosition> getBattingPositions();
    BattingPosition saveBattingPosition(BattingPosition pos);
    Optional<BattingPosition> findBattingPositionByCode(String code);
    void deleteBattingPosition(String code);

    // Bowling Styles
    List<BowlingStyle> getBowlingStyles();
    BowlingStyle saveBowlingStyle(BowlingStyle style);
    Optional<BowlingStyle> findBowlingStyleByCode(String code);
    void deleteBowlingStyle(String code);

    // Competitive Levels
    List<CompetitiveLevel> getCompetitiveLevels();
    CompetitiveLevel saveCompetitiveLevel(CompetitiveLevel level);
    Optional<CompetitiveLevel> findCompetitiveLevelByCode(String code);
    void deleteCompetitiveLevel(String code);

    // Education Statuses
    List<EducationStatus> getEducationStatuses();
    EducationStatus saveEducationStatus(EducationStatus status);
    Optional<EducationStatus> findEducationStatusByCode(String code);
    void deleteEducationStatus(String code);

    // Schools
    List<School> getSchools(String search);
    School saveSchool(School school);
    Optional<School> findSchoolById(String id);
    void deleteSchool(String id);

    // Locations (Hierarchy)
    List<Division> getLocations();
    Division saveDivision(Division division);
    Optional<Division> findDivisionById(String id);
    void deleteDivision(String id);
    
    District saveDistrict(String divisionId, District district);
    Optional<District> findDistrictById(String id);
    void deleteDistrict(String id);
    
    Upazila saveUpazila(String districtId, Upazila upazila);
    Optional<Upazila> findUpazilaById(String id);
    void deleteUpazila(String id);
    
    Union saveUnion(String upazilaId, Union union);
    Optional<Union> findUnionById(String id);
    void deleteUnion(String id);
}
