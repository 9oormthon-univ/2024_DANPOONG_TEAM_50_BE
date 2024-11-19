package com.example.mymoo.global.enums;

public enum UserRole {
    DONATOR("DONATOR"),
    CHILD("CHILD"),
    STORE("STORE"),
    ADMIN("ADMIN");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
