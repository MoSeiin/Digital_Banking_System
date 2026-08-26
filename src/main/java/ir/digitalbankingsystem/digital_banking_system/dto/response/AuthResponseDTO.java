package ir.digitalbankingsystem.digital_banking_system.dto.response;

import ir.digitalbankingsystem.digital_banking_system.domain.Role;
import ir.digitalbankingsystem.digital_banking_system.domain.Status;

import java.util.UUID;

public record AuthResponseDTO(
        String token,
        UUID userCode,
        String userName,
        Role role,
        Status status
) {
}
