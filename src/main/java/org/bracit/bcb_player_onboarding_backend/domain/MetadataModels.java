package org.bracit.bcb_player_onboarding_backend.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MetadataModels {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayingRole {
        private String code;
        private String nameEn;
        private String nameBn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BattingPosition {
        private String code;
        private String nameEn;
        private String nameBn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BowlingStyle {
        private String code;
        private String nameEn;
        private String nameBn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetitiveLevel {
        private String code;
        private String nameEn;
        private String nameBn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducationStatus {
        private String code;
        private String nameEn;
        private String nameBn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class School {
        private String id;
        private String nameEn;
        private String nameBn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Division {
        private String id;
        private String nameEn;
        private String nameBn;
        @Builder.Default
        private List<District> districts = new ArrayList<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class District {
        private String id;
        private String nameEn;
        private String nameBn;
        @Builder.Default
        private List<Upazila> upazilas = new ArrayList<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Upazila {
        private String id;
        private String nameEn;
        private String nameBn;
        @Builder.Default
        private List<Union> unions = new ArrayList<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Union {
        private String id;
        private String nameEn;
        private String nameBn;
    }
}
