package ir.digitalbankingsystem.digital_banking_system.dto.request;

import ir.digitalbankingsystem.digital_banking_system.domain.Gender;
import ir.digitalbankingsystem.digital_banking_system.domain.Role;
import jakarta.validation.constraints.*;

public record RegisterRequestDTO(
        String firstName,

        String lastName,

        @NotBlank
        String userName,
        @Email
        String email,
        @NotBlank
        String password,
        String phoneNumber,
        String address,
        @Positive
        @Min(18)
        Integer age,
        Role role ,
        Gender gender


) {
}
