package ir.digitalbankingsystem.digital_banking_system.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ir.digitalbankingsystem.digital_banking_system.dto.response.CardResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.service.CardService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CardController {

    private final CardService cardService;

    @PostMapping("/account/{accountNumber}/issue")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<CardResponseDTO> issueCard(@PathVariable String accountNumber) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.issueCard(accountNumber));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Page<CardResponseDTO>> getMyCards(Pageable pageable) {
        return ResponseEntity.ok(cardService.getMyCards(pageable));
    }

    @PatchMapping("/{cardNumber}/block")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<CardResponseDTO> blockCard(@PathVariable String cardNumber) {
        return ResponseEntity.ok(cardService.blockCard(cardNumber));
    }

    @PatchMapping("/{cardNumber}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<CardResponseDTO> activateCard(@PathVariable String cardNumber) {
        return ResponseEntity.ok(cardService.activateCard(cardNumber));
    }
}
