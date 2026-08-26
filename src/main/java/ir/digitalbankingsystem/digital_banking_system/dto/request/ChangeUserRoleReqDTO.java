package ir.digitalbankingsystem.digital_banking_system.dto.request;

import ir.digitalbankingsystem.digital_banking_system.domain.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeUserRoleReqDTO(
        @NotNull
        Role role
) {
}
