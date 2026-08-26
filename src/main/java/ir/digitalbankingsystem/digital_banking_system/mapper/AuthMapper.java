package ir.digitalbankingsystem.digital_banking_system.mapper;

import ir.digitalbankingsystem.digital_banking_system.domain.Admin;
import ir.digitalbankingsystem.digital_banking_system.domain.Customer;
import ir.digitalbankingsystem.digital_banking_system.domain.Employee;
import ir.digitalbankingsystem.digital_banking_system.domain.Person;
import ir.digitalbankingsystem.digital_banking_system.dto.request.RegisterRequestDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AuthResponseDTO;
import jakarta.validation.constraints.Min;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "status" , ignore = true)
    @Mapping(target = "userCode" ,ignore = true )
    @Mapping(target = "createAt" , ignore = true)
    Person toPerson(RegisterRequestDTO dto);

    AuthResponseDTO toAuthResponseDTO(Person person);


    @ObjectFactory
    default Person createPerson(RegisterRequestDTO dto) {
        return switch (dto.role()) {
            case ADMIN -> new Admin();
            case CUSTOMER ->  new Customer();
            case EMPLOYEE ->  new Employee();
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
