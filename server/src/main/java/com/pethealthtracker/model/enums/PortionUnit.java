package com.pethealthtracker.model.enums;

public enum PortionUnit {
    G("g"),
    KG("kg"),
    CUP("cup"),
    ML("ml"),
    OZ("oz");

    private final String value;

    PortionUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PortionUnit fromValue(String value) {
        for (PortionUnit unit : values()) {
            if (unit.value.equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unknown PortionUnit value: " + value);
    }
}
