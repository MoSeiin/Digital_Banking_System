package ir.digitalbankingsystem.digital_banking_system.dto.response;

import ir.digitalbankingsystem.digital_banking_system.domain.AccountStatus;
import ir.digitalbankingsystem.digital_banking_system.domain.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AccountResponseDTO(
        String accountNumber,
        BigDecimal balance,
        Currency currency,
        AccountStatus status,
        LocalDateTime createdAt,
        List<CardResponseDTO> cards
) {
}
