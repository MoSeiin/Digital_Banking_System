package ir.digitalbankingsystem.digital_banking_system.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum Gender {
    MALE,
    FEMALE;

    @JsonCreator
    public static Gender fromValue(String value) {
        return Gender.valueOf(
                value.trim().toUpperCase(Locale.ROOT)
        );
    }
}
