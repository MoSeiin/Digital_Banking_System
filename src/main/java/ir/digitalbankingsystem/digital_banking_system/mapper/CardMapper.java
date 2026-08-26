package ir.digitalbankingsystem.digital_banking_system.mapper;

import ir.digitalbankingsystem.digital_banking_system.domain.Card;
import ir.digitalbankingsystem.digital_banking_system.dto.response.CardResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "accountNumber", source = "account.accountNumber")
    CardResponseDTO toResponseDTO(Card card);
}
