package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.dto.request.TransactionFilterDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.TransactionReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionService {

        TransactionResponseDTO deposit(
                TransactionReqDTO.DepositReqDTO request
        );

        TransactionResponseDTO withdraw(
                TransactionReqDTO.WithdrawReqDTO request
        );

        TransactionResponseDTO transfer(
                TransactionReqDTO.TransferReqDTO request
        );

        Page<TransactionResponseDTO> getMyTransactions(
                TransactionFilterDTO filter,
                Pageable pageable
        );

        Page<TransactionResponseDTO> getPersonTransactions(
                UUID personCode,
                TransactionFilterDTO filter,
                Pageable pageable
        );

}
