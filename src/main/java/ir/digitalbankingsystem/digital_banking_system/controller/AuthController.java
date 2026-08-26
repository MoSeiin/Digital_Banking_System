package ir.digitalbankingsystem.digital_banking_system.controller;

import ir.digitalbankingsystem.digital_banking_system.dto.request.LoginRequestDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.RegisterRequestDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AuthResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register( @RequestBody @Valid RegisterRequestDTO signUpRequestDTO) {
        AuthResponseDTO register = authService.register(signUpRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(register);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        AuthResponseDTO login = authService.login(loginRequestDTO);
        return ResponseEntity.ok(login);
    }
}
