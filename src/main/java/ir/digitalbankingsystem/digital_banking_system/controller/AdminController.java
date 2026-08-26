package ir.digitalbankingsystem.digital_banking_system.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ir.digitalbankingsystem.digital_banking_system.dto.request.ChangeUserRoleReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.TransactionFilterDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AdminUserResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.TransactionResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.TransactionSummaryResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserResponseDTO>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getUsers(pageable));
    }

    @GetMapping("/users/{userCode}")
    public ResponseEntity<AdminUserResponseDTO> getUser(@PathVariable UUID userCode) {
        return ResponseEntity.ok(adminService.getUser(userCode));
    }

    @PutMapping("/user/{userCode}/approve")
    public ResponseEntity<Void> approveUser(@PathVariable UUID userCode) {
        adminService.approveUser(userCode);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{userCode}/reject")
    public ResponseEntity<Void> rejectUser(@PathVariable UUID userCode) {
        adminService.rejectUser(userCode);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{userCode}/block")
    public ResponseEntity<Void> blockUser(@PathVariable UUID userCode) {
        adminService.blockUser(userCode);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{userCode}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable UUID userCode) {
        adminService.unblockUser(userCode);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{userCode}/role")
    public ResponseEntity<AdminUserResponseDTO> changeRole(
            @PathVariable UUID userCode,
            @Valid @RequestBody ChangeUserRoleReqDTO request) {
        return ResponseEntity.ok(adminService.changeRole(userCode, request));
    }

    @GetMapping("/transactions")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactions(
            @ModelAttribute TransactionFilterDTO filter, Pageable pageable) {
        return ResponseEntity.ok(adminService.getTransactions(filter, pageable));
    }

    @GetMapping("/reports/transactions/summary")
    public ResponseEntity<TransactionSummaryResponseDTO> getTransactionSummary() {
        return ResponseEntity.ok(adminService.getTransactionSummary());
    }
}
