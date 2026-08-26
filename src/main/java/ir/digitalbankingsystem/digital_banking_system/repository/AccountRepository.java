package ir.digitalbankingsystem.digital_banking_system.repository;

import ir.digitalbankingsystem.digital_banking_system.domain.Account;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Page<Account> findAllByPerson_UserCode(UUID userCode, Pageable pageable);

    Optional<Account> findByAccountNumber(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT a
            FROM Account a
            WHERE a.accountNumber = :accountNumber
            """)
    Optional<Account> findByAccountNumberForUpdate(
            @Param("accountNumber") String accountNumber
    );
}
