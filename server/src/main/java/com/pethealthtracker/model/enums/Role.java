package com.pethealthtracker.model.enums;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;
    
    public static Role fromBoolean(boolean isAdmin) {
        return isAdmin ? ROLE_ADMIN : ROLE_USER;
    }
    
    @Override
    public String toString() {
        return this.name();
    }
}
