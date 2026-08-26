package ir.digitalbankingsystem.digital_banking_system.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Accounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account-seq")
    @SequenceGenerator(name = "account-seq", allocationSize = 10, sequenceName = "account-seq")
    @Column(nullable = false, updatable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String accountNumber;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.balance == null) this.balance = BigDecimal.ZERO;
        if (this.currency == null) this.currency = Currency.IRR;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id" , nullable = false)
    private Person person;

    @OneToMany(mappedBy = "fromAccount", fetch = FetchType.LAZY)
    private List<Transaction> outgoingTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "toAccount", fetch = FetchType.LAZY)
    private List<Transaction> incomingTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();


}
