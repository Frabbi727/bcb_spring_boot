package org.bracit.bcb_player_onboarding_backend.domain;

public enum PlayerPosition {
    BATSMAN,
    BOWLER,
    WICKETKEEPER,
    ALL_ROUNDER;

    public static PlayerPosition fromString(String value) {
        if (value == null) return null;
        try {
            return PlayerPosition.valueOf(value.toUpperCase().replace("-", "_").trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
