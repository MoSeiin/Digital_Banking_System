package ir.digitalbankingsystem.digital_banking_system.controller;

import ir.digitalbankingsystem.digital_banking_system.dto.request.ChangePasswordReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.UpdateUserReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AuthResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.service.UpdateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("auth/update")
public class UpdateController {

    private final UpdateService updateService;

    public UpdateController(UpdateService updateService) {
        this.updateService = updateService;
    }

    @PutMapping("/user/{userCode}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthResponseDTO> updateUser(@PathVariable UUID userCode, @Valid @RequestBody UpdateUserReqDTO updateUserReqDTO) {
        AuthResponseDTO authResponseDTO = updateService.updateUser(userCode, updateUserReqDTO);
        return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);
    }

    @PatchMapping("/{userCode}/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(@PathVariable UUID userCode, @Valid @RequestBody ChangePasswordReqDTO request) {
        updateService.changePassword(userCode, request);
        return ResponseEntity.noContent().build();
    }
}
