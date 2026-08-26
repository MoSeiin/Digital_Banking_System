package ir.digitalbankingsystem.digital_banking_system.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum Type {
    DEPOSIT,
    WITHDRAW,
            TRANSFER;

    @JsonCreator
    public static Type fromValue(String value) {
        return Type.valueOf(
                value.trim().toUpperCase(Locale.ROOT)
        );
    }
}
