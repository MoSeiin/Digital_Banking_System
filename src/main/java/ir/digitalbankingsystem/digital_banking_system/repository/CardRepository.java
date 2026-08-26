package ir.digitalbankingsystem.digital_banking_system.repository;

import ir.digitalbankingsystem.digital_banking_system.domain.Card;
import ir.digitalbankingsystem.digital_banking_system.domain.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(String cardNumber);
    boolean existsByCardNumber(String cardNumber);
    org.springframework.data.domain.Page<Card> findAllByAccount_Person_UserCode(UUID userCode, org.springframework.data.domain.Pageable pageable);
    boolean existsByAccount_IdAndStatus(Long accountId, CardStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Card c where c.cardNumber = :cardNumber")
    Optional<Card> findByCardNumberForUpdate(@Param("cardNumber") String cardNumber);
}
