package ir.digitalbankingsystem.digital_banking_system.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ir.digitalbankingsystem.digital_banking_system.dto.request.TransactionFilterDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.TransactionReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.TransactionResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/deposit")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<TransactionResponseDTO> deposit(@Valid @RequestBody TransactionReqDTO.DepositReqDTO request) {

        TransactionResponseDTO response = transactionService.deposit(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PostMapping("/withdraw")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<TransactionResponseDTO> withdraw(@Valid @RequestBody TransactionReqDTO.WithdrawReqDTO request
    ) {

        TransactionResponseDTO response =
                transactionService.withdraw(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'EMPLOYEE')")
    public ResponseEntity<TransactionResponseDTO> transfer(@Valid @RequestBody TransactionReqDTO.TransferReqDTO request
    ) {

        TransactionResponseDTO response =
                transactionService.transfer(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/my-history")
    @PreAuthorize(
            "hasAnyRole('CUSTOMER', 'ADMIN', 'EMPLOYEE')"
    )
    public ResponseEntity<Page<TransactionResponseDTO>>
    getMyTransactions(

            @ModelAttribute
            TransactionFilterDTO filter,

            Pageable pageable

    ) {

        return ResponseEntity.ok(
                transactionService.getMyTransactions(
                        filter,
                        pageable
                )
        );
    }

    @GetMapping("/person/{personCode}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Page<TransactionResponseDTO>>
    getPersonTransactions(

            @PathVariable
            UUID personCode,

            @ModelAttribute
            TransactionFilterDTO filter,

            Pageable pageable

    ) {

        return ResponseEntity.ok(
                transactionService.getPersonTransactions(
                        personCode,
                        filter,
                        pageable
                )
        );
    }
}