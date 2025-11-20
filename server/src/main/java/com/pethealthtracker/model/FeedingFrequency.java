package com.pethealthtracker.model;

public enum FeedingFrequency {
    DAILY_ONCE("daily_once"),
    DAILY_TWICE("daily_twice"),
    DAILY_THRICE("daily_thrice"),
    CUSTOM("custom");

    private final String value;

    FeedingFrequency(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FeedingFrequency fromValue(String value) {
        for (FeedingFrequency frequency : values()) {
            if (frequency.value.equalsIgnoreCase(value)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("Unknown FeedingFrequency value: " + value);
    }
}
