package ir.digitalbankingsystem.digital_banking_system.dto.response;

import ir.digitalbankingsystem.digital_banking_system.domain.Gender;
import ir.digitalbankingsystem.digital_banking_system.domain.Role;
import ir.digitalbankingsystem.digital_banking_system.domain.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminUserResponseDTO(
        UUID userCode,
        String firstName,
        String lastName,
        String userName,
        String email,
        String phoneNumber,
        Integer age,
        Gender gender,
        Role role,
        Status status,
        LocalDateTime createdAt
) {
}
