package ir.digitalbankingsystem.digital_banking_system.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionReqDTO() {

    public record DepositReqDTO(
            @NotBlank
            String accountNumber,
            @NotNull
            @NotNull
            @DecimalMin(value = "0.01")
            BigDecimal amount
    ) {}

    public record WithdrawReqDTO(
            @NotBlank
            String accountNumber,
            @NotNull
            @NotNull
            @DecimalMin(value = "0.01")
            BigDecimal amount
    ) {}

    public record TransferReqDTO(
            @NotBlank
            String fromAccountNumber,
            @NotBlank
            String toAccountNumber,
            @NotNull
            @DecimalMin(value = "0.01")
            BigDecimal amount
    ) {}
}
