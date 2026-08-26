package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.dto.response.AccountResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

public interface AccountService {

        AccountResponseDTO createAccount(UUID personCode);

        Page<AccountResponseDTO> getMyAccounts(Pageable pageable);
        AccountResponseDTO closeAccount( String accountNumber);
}
