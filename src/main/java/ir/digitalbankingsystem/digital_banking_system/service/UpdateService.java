package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.dto.request.ChangePasswordReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.UpdateUserReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AuthResponseDTO;

import java.util.UUID;

public interface UpdateService {
    AuthResponseDTO updateUser(UUID userCode, UpdateUserReqDTO updateUserReqDTO);

    void changePassword(UUID userCode , ChangePasswordReqDTO changePasswordReqDTO);
}
