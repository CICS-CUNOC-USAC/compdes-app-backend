package com.compdes.activity.enums;

import lombok.Getter;

@Getter
public enum ActivityType {
    MEETING("Meeting"),
    WORKSHOP("Taller"),
    PRESENTATION("Ponencia");
    private final String displayName;
    ActivityType(String displayName) {
        this.displayName = displayName;
    }
}
