package ir.digitalbankingsystem.digital_banking_system.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
