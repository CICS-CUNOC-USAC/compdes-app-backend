package com.compdes.activity.enums;

import lombok.Getter;

@Getter
public enum ActivityType {
    MEETING("Meeting"),
    WORKSHOP("Taller"),
    PRESENTATION("Presentación");
    private final String displayName;
    ActivityType(String displayName) {
        this.displayName = displayName;
    }
}
