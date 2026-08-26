package ir.digitalbankingsystem.digital_banking_system.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum TransactionStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REVERSED  ;

    @JsonCreator
    public static TransactionStatus forValue(String value) {
        return TransactionStatus.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}
