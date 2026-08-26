package ir.digitalbankingsystem.digital_banking_system.repository;

import ir.digitalbankingsystem.digital_banking_system.domain.Transaction;
import ir.digitalbankingsystem.digital_banking_system.domain.TransactionStatus;
import ir.digitalbankingsystem.digital_banking_system.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    long countByStatus(TransactionStatus status);

    @Query("select coalesce(sum(t.amount), 0) from Transaction t where t.type = :type and t.status = :status")
    BigDecimal sumAmountByTypeAndStatus(@Param("type") Type type, @Param("status") TransactionStatus status);
}
