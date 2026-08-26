package ir.digitalbankingsystem.digital_banking_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
        @Email
        String email ,

        String userName ,
        @NotNull
        String password
) {
}
