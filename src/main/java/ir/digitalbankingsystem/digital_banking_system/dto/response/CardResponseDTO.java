package ir.digitalbankingsystem.digital_banking_system.dto.response;

import ir.digitalbankingsystem.digital_banking_system.domain.CardStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CardResponseDTO(
        String cardNumber,
        String accountNumber,
        LocalDate expiryDate,
        CardStatus status,
        LocalDateTime issuedAt
) {
}
