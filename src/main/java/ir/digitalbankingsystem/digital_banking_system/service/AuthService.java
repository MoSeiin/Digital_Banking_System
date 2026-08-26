package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.dto.request.LoginRequestDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.RegisterRequestDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO registerRequestDTO);

    AuthResponseDTO login(LoginRequestDTO login);
}
