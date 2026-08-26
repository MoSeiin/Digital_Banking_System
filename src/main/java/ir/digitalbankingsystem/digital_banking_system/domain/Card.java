package ir.digitalbankingsystem.digital_banking_system.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 16)
    private String cardNumber;

    @Column(nullable = false, updatable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime issuedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @PrePersist
    public void prePersist() {
        if (issuedAt == null) {
            issuedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = CardStatus.ACTIVE;
        }
    }
}
