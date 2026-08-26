package ir.digitalbankingsystem.digital_banking_system.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum Role {
    CUSTOMER,
    ADMIN,
    EMPLOYEE;

    @JsonCreator
    public static Role fromValue(String value) {
        return Role.valueOf(
                value.trim().toUpperCase(Locale.ROOT)
        );
    }
}