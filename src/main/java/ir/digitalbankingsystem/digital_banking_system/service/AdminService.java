package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.domain.Role;
import ir.digitalbankingsystem.digital_banking_system.dto.request.ChangeUserRoleReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.TransactionFilterDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AdminUserResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.TransactionResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.TransactionSummaryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminService {
    void approveUser(UUID userCode);
    void rejectUser(UUID userCode);
    void blockUser(UUID userCode);
    void unblockUser(UUID userCode);
    Page<AdminUserResponseDTO> getUsers(Pageable pageable);
    AdminUserResponseDTO getUser(UUID userCode);
    AdminUserResponseDTO changeRole(UUID userCode, ChangeUserRoleReqDTO request);
    Page<TransactionResponseDTO> getTransactions(TransactionFilterDTO filter, Pageable pageable);
    TransactionSummaryResponseDTO getTransactionSummary();
}
