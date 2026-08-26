package ir.digitalbankingsystem.digital_banking_system.dto.request;

import ir.digitalbankingsystem.digital_banking_system.domain.TransactionStatus;
import ir.digitalbankingsystem.digital_banking_system.domain.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionFilterDTO(Type type,

                                   TransactionStatus status,

                                   BigDecimal minAmount,

                                   BigDecimal maxAmount,

                                   LocalDateTime fromDate,

                                   LocalDateTime toDate

) {
}
