package ir.digitalbankingsystem.digital_banking_system.mapper;

import ir.digitalbankingsystem.digital_banking_system.domain.Transaction;
import ir.digitalbankingsystem.digital_banking_system.dto.response.TransactionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "fromAccountNumber", source = "fromAccount.accountNumber")
    @Mapping(target = "toAccountNumber", source = "toAccount.accountNumber")
    TransactionResponseDTO toResponseDTO(Transaction transaction);
}
