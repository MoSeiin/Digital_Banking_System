package ir.digitalbankingsystem.digital_banking_system.dto.response;

import ir.digitalbankingsystem.digital_banking_system.domain.TransactionStatus;
import ir.digitalbankingsystem.digital_banking_system.domain.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        String referenceNumber,
        BigDecimal amount,
        String fromAccountNumber,
        String toAccountNumber,
        Type type,
        TransactionStatus status,
        LocalDateTime createdAt
) {
}
