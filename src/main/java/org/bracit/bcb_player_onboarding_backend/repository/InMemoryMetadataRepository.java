package org.bracit.bcb_player_onboarding_backend.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.bracit.bcb_player_onboarding_backend.domain.MetadataModels.*;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMetadataRepository implements MetadataRepository {

    private final Map<String, PlayingRole> roles = new ConcurrentHashMap<>();
    private final Map<String, BattingPosition> battingPositions = new ConcurrentHashMap<>();
    private final Map<String, BowlingStyle> bowlingStyles = new ConcurrentHashMap<>();
    private final Map<String, CompetitiveLevel> competitiveLevels = new ConcurrentHashMap<>();
    private final Map<String, EducationStatus> educationStatuses = new ConcurrentHashMap<>();
    private final Map<String, School> schools = new ConcurrentHashMap<>();
    private final Map<String, Division> divisions = new ConcurrentHashMap<>();

    public InMemoryMetadataRepository() {
        // Initialize default playing roles
        savePlayingRole(new PlayingRole("ROLE_ALLROUNDER", "Allrounder", "অলরাউন্ডার"));
        savePlayingRole(new PlayingRole("ROLE_BATSMAN", "Batsman", "ব্যাটসম্যান"));
        savePlayingRole(new PlayingRole("ROLE_BOWLER", "Bowler", "বোলার"));

        // Initialize batting positions
        saveBattingPosition(new BattingPosition("BAT_POS_OPENER", "Opening Batsman", "ওপেনার"));
        saveBattingPosition(new BattingPosition("BAT_POS_MIDDLE_ORDER", "Middle Order", "মিডল অর্ডার"));
        saveBattingPosition(new BattingPosition("BAT_POS_TAIL_ENDER", "Tail-ender", "টেইল-এন্ডার"));

        // Initialize bowling styles
        saveBowlingStyle(new BowlingStyle("BOWL_STYLE_OFFBREAK", "Right-arm Offbreak", "রাইট-আর্ম অফব্রেক"));
        saveBowlingStyle(new BowlingStyle("BOWL_STYLE_PACER", "Right-arm Fast Medium", "রাইট-আর্ম ফাস্ট মিডিয়াম"));
        saveBowlingStyle(new BowlingStyle("BOWL_STYLE_LEFT_ORTHO", "Left-arm Orthodox", "লেফট-আর্ম অর্থোডক্স"));

        // Initialize competitive levels
        saveCompetitiveLevel(new CompetitiveLevel("COMP_LEVEL_DISTRICT", "District League", "জেলা লীগ"));
        saveCompetitiveLevel(new CompetitiveLevel("COMP_LEVEL_DIVISION", "Divisional League", "বিভাগীয় লীগ"));
        saveCompetitiveLevel(new CompetitiveLevel("COMP_LEVEL_NATIONAL", "National Team", "জাতীয় দল"));

        // Initialize education statuses
        saveEducationStatus(new EducationStatus("EDU_STATUS_STUDYING", "Studying", "অধ্যয়নরত"));
        saveEducationStatus(new EducationStatus("EDU_STATUS_COMPLETED", "Completed", "সম্পন্ন"));

        // Initialize schools
        saveSchool(new School("sch_dhaka_01", "Mirpur High School", "মিরপুর হাই স্কুল"));
        saveSchool(new School("sch_dhaka_02", "Tongi Govt. School", "টঙ্গী সরকারী স্কুল"));

        // Initialize default division with districts, upazilas and unions
        Division dhaka = new Division("div_dhaka", "Dhaka", "ঢাকা", new ArrayList<>());
        saveDivision(dhaka);
        
        District distDhaka = new District("dist_dhaka", "Dhaka", "ঢাকা", new ArrayList<>());
        saveDistrict("div_dhaka", distDhaka);
        
        Upazila mirpur = new Upazila("upa_mirpur", "Mirpur", "মিরপুর", new ArrayList<>());
        saveUpazila("dist_dhaka", mirpur);
        
        Union section12 = new Union("uni_section12", "Section 12", "সেকশন ১২");
        saveUnion("upa_mirpur", section12);
    }

    // Playing Roles CRUD
    @Override
    public List<PlayingRole> getPlayingRoles() {
        return new ArrayList<>(roles.values());
    }
    @Override
    public PlayingRole savePlayingRole(PlayingRole role) {
        roles.put(role.getCode(), role);
        return role;
    }
    @Override
    public Optional<PlayingRole> findPlayingRoleByCode(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(roles.get(code));
    }
    @Override
    public void deletePlayingRole(String code) {
        if (code != null) roles.remove(code);
    }

    // Batting Positions CRUD
    @Override
    public List<BattingPosition> getBattingPositions() {
        return new ArrayList<>(battingPositions.values());
    }
    @Override
    public BattingPosition saveBattingPosition(BattingPosition pos) {
        battingPositions.put(pos.getCode(), pos);
        return pos;
    }
    @Override
    public Optional<BattingPosition> findBattingPositionByCode(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(battingPositions.get(code));
    }
    @Override
    public void deleteBattingPosition(String code) {
        if (code != null) battingPositions.remove(code);
    }

    // Bowling Styles CRUD
    @Override
    public List<BowlingStyle> getBowlingStyles() {
        return new ArrayList<>(bowlingStyles.values());
    }
    @Override
    public BowlingStyle saveBowlingStyle(BowlingStyle style) {
        bowlingStyles.put(style.getCode(), style);
        return style;
    }
    @Override
    public Optional<BowlingStyle> findBowlingStyleByCode(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(bowlingStyles.get(code));
    }
    @Override
    public void deleteBowlingStyle(String code) {
        if (code != null) bowlingStyles.remove(code);
    }

    // Competitive Levels CRUD
    @Override
    public List<CompetitiveLevel> getCompetitiveLevels() {
        return new ArrayList<>(competitiveLevels.values());
    }
    @Override
    public CompetitiveLevel saveCompetitiveLevel(CompetitiveLevel level) {
        competitiveLevels.put(level.getCode(), level);
        return level;
    }
    @Override
    public Optional<CompetitiveLevel> findCompetitiveLevelByCode(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(competitiveLevels.get(code));
    }
    @Override
    public void deleteCompetitiveLevel(String code) {
        if (code != null) competitiveLevels.remove(code);
    }

    // Education Statuses CRUD
    @Override
    public List<EducationStatus> getEducationStatuses() {
        return new ArrayList<>(educationStatuses.values());
    }
    @Override
    public EducationStatus saveEducationStatus(EducationStatus status) {
        educationStatuses.put(status.getCode(), status);
        return status;
    }
    @Override
    public Optional<EducationStatus> findEducationStatusByCode(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(educationStatuses.get(code));
    }
    @Override
    public void deleteEducationStatus(String code) {
        if (code != null) educationStatuses.remove(code);
    }

    // Schools CRUD
    @Override
    public List<School> getSchools(String search) {
        if (search == null || search.trim().isEmpty()) {
            return new ArrayList<>(schools.values());
        }
        String q = search.toLowerCase().trim();
        return schools.values().stream()
                .filter(s -> s.getNameEn().toLowerCase().contains(q) || s.getNameBn().contains(q))
                .collect(Collectors.toList());
    }
    @Override
    public School saveSchool(School school) {
        schools.put(school.getId(), school);
        return school;
    }
    @Override
    public Optional<School> findSchoolById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(schools.get(id));
    }
    @Override
    public void deleteSchool(String id) {
        if (id != null) schools.remove(id);
    }

    // Divisions
    @Override
    public List<Division> getLocations() {
        return new ArrayList<>(divisions.values());
    }
    @Override
    public Division saveDivision(Division division) {
        divisions.put(division.getId(), division);
        return division;
    }
    @Override
    public Optional<Division> findDivisionById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(divisions.get(id));
    }
    @Override
    public void deleteDivision(String id) {
        if (id != null) divisions.remove(id);
    }

    // Districts
    @Override
    public District saveDistrict(String divisionId, District district) {
        Division div = divisions.get(divisionId);
        if (div != null) {
            div.getDistricts().removeIf(d -> d.getId().equals(district.getId()));
            div.getDistricts().add(district);
        }
        return district;
    }
    @Override
    public Optional<District> findDistrictById(String id) {
        if (id == null) return Optional.empty();
        for (Division div : divisions.values()) {
            for (District dist : div.getDistricts()) {
                if (id.equals(dist.getId())) return Optional.of(dist);
            }
        }
        return Optional.empty();
    }
    @Override
    public void deleteDistrict(String id) {
        if (id == null) return;
        for (Division div : divisions.values()) {
            div.getDistricts().removeIf(d -> id.equals(d.getId()));
        }
    }

    // Upazilas
    @Override
    public Upazila saveUpazila(String districtId, Upazila upazila) {
        Optional<District> distOpt = findDistrictById(districtId);
        if (distOpt.isPresent()) {
            District dist = distOpt.get();
            dist.getUpazilas().removeIf(u -> u.getId().equals(upazila.getId()));
            dist.getUpazilas().add(upazila);
        }
        return upazila;
    }
    @Override
    public Optional<Upazila> findUpazilaById(String id) {
        if (id == null) return Optional.empty();
        for (Division div : divisions.values()) {
            for (District dist : div.getDistricts()) {
                for (Upazila upa : dist.getUpazilas()) {
                    if (id.equals(upa.getId())) return Optional.of(upa);
                }
            }
        }
        return Optional.empty();
    }
    @Override
    public void deleteUpazila(String id) {
        if (id == null) return;
        for (Division div : divisions.values()) {
            for (District dist : div.getDistricts()) {
                dist.getUpazilas().removeIf(u -> id.equals(u.getId()));
            }
        }
    }

    // Unions
    @Override
    public Union saveUnion(String upazilaId, Union union) {
        Optional<Upazila> upaOpt = findUpazilaById(upazilaId);
        if (upaOpt.isPresent()) {
            Upazila upa = upaOpt.get();
            upa.getUnions().removeIf(u -> u.getId().equals(union.getId()));
            upa.getUnions().add(union);
        }
        return union;
    }
    @Override
    public Optional<Union> findUnionById(String id) {
        if (id == null) return Optional.empty();
        for (Division div : divisions.values()) {
            for (District dist : div.getDistricts()) {
                for (Upazila upa : dist.getUpazilas()) {
                    for (Union uni : upa.getUnions()) {
                        if (id.equals(uni.getId())) return Optional.of(uni);
                    }
                }
            }
        }
        return Optional.empty();
    }
    @Override
    public void deleteUnion(String id) {
        if (id == null) return;
        for (Division div : divisions.values()) {
            for (District dist : div.getDistricts()) {
                for (Upazila upa : dist.getUpazilas()) {
                    upa.getUnions().removeIf(u -> id.equals(u.getId()));
                }
            }
        }
    }
}
