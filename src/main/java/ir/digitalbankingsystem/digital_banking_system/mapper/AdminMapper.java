package ir.digitalbankingsystem.digital_banking_system.mapper;

import ir.digitalbankingsystem.digital_banking_system.domain.Person;
import ir.digitalbankingsystem.digital_banking_system.dto.request.UpdateUserReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AuthResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.bind.annotation.PathVariable;

@Mapper(componentModel = "spring" , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdminMapper {
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "userCode" , ignore = true)
    @Mapping(target = "password" , ignore = true)
    void updatePerson(UpdateUserReqDTO updateUserReqDTO, @MappingTarget Person person);

    AuthResponseDTO toAuthResponseDTO(Person person);
}
