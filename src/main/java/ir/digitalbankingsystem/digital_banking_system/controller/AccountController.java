package ir.digitalbankingsystem.digital_banking_system.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AccountResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/person/{personCode}/create")
   @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AccountResponseDTO> createAccount(@PathVariable UUID personCode) {
        AccountResponseDTO response = accountService.createAccount(personCode);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Page<AccountResponseDTO>> getMyAccounts(
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                accountService.getMyAccounts(pageable)
        );
    }

    @PatchMapping("/{accountNumber}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AccountResponseDTO> closeAccount(
            @PathVariable String accountNumber
    ) {
        return ResponseEntity.ok(
                accountService.closeAccount(accountNumber)
        );
    }
}
