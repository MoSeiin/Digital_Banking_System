package ir.digitalbankingsystem.digital_banking_system.dto.response;

import java.math.BigDecimal;

public record TransactionSummaryResponseDTO(
        long totalTransactions,
        long successfulTransactions,
        long failedTransactions,
        BigDecimal totalDeposits,
        BigDecimal totalWithdrawals,
        BigDecimal totalTransfers
) {
}
