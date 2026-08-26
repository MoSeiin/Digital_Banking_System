package ir.digitalbankingsystem.digital_banking_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordReqDTO(
        @NotBlank
        String oldPassword,

        @NotBlank
        @Size(min = 8, max = 32)
        String newPassword,

        @NotBlank
        String confirmPassword
) {
}
