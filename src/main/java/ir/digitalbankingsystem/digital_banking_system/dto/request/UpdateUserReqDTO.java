package ir.digitalbankingsystem.digital_banking_system.dto.request;

import ir.digitalbankingsystem.digital_banking_system.domain.Gender;
import ir.digitalbankingsystem.digital_banking_system.domain.Role;

public record UpdateUserReqDTO(
        String firstName,
        String lastName,
        Gender gender ,
        Integer age

) {
}
