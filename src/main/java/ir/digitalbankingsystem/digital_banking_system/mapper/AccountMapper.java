package ir.digitalbankingsystem.digital_banking_system.mapper;

import ir.digitalbankingsystem.digital_banking_system.domain.Account;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AccountResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CardMapper.class)
public interface AccountMapper {

    AccountResponseDTO toAccountResponseDTO(Account account);
}
