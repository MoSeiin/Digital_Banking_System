package ir.digitalbankingsystem.digital_banking_system.specification;

import ir.digitalbankingsystem.digital_banking_system.domain.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TransactionSpecification {

    private TransactionSpecification() {
    }

    public static Specification<Transaction> belongsToPerson(
            UUID personCode
    ) {

        return (root, query, criteriaBuilder) -> {

            Join<Transaction, Account> fromAccount =
                    root.join(
                            "fromAccount",
                            JoinType.LEFT
                    );

            Join<Transaction, Account> toAccount =
                    root.join(
                            "toAccount",
                            JoinType.LEFT
                    );

            Join<Account, Person> fromPerson =
                    fromAccount.join(
                            "person",
                            JoinType.LEFT
                    );

            Join<Account, Person> toPerson =
                    toAccount.join(
                            "person",
                            JoinType.LEFT
                    );

            return criteriaBuilder.or(

                    criteriaBuilder.equal(
                            fromPerson.get("userCode"),
                            personCode
                    ),

                    criteriaBuilder.equal(
                            toPerson.get("userCode"),
                            personCode
                    )
            );
        };
    }

    public static Specification<Transaction> hasType(
            Type type
    ) {

        return (root, query, criteriaBuilder) -> {

            if (type == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("type"),
                    type
            );
        };
    }

    public static Specification<Transaction> hasStatus(
            TransactionStatus status
    ) {

        return (root, query, criteriaBuilder) -> {

            if (status == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("status"),
                    status
            );
        };
    }

    public static Specification<Transaction> amountGreaterThanOrEqual(
            BigDecimal minAmount
    ) {

        return (root, query, criteriaBuilder) -> {

            if (minAmount == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("amount"),
                    minAmount
            );
        };
    }

    public static Specification<Transaction> amountLessThanOrEqual(
            BigDecimal maxAmount
    ) {

        return (root, query, criteriaBuilder) -> {

            if (maxAmount == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("amount"),
                    maxAmount
            );
        };
    }

    public static Specification<Transaction> createdAtAfter(
            LocalDateTime fromDate
    ) {

        return (root, query, criteriaBuilder) -> {

            if (fromDate == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("createdAt"),
                    fromDate
            );
        };
    }

    public static Specification<Transaction> createdAtBefore(
            LocalDateTime toDate
    ) {

        return (root, query, criteriaBuilder) -> {

            if (toDate == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("createdAt"),
                    toDate
            );
        };
    }
}
