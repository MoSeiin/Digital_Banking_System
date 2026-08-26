package ir.digitalbankingsystem.digital_banking_system.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person-seq")
    @SequenceGenerator(name = "person-seq", allocationSize = 10, sequenceName = "person-seq")
    @Column(nullable = false, updatable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true, updatable = false)
    @Setter(AccessLevel.PRIVATE)
    private UUID userCode;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    private Integer age;

    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "person")
    private Set<Account> accounts = new HashSet<>();


    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
        if (this.userCode == null) this.userCode = UUID.randomUUID();
        if (this.role != Role.ADMIN) this.status = Status.PENDING;


    }


}
