package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.config.SecurityUtils;
import ir.digitalbankingsystem.digital_banking_system.domain.*;
import ir.digitalbankingsystem.digital_banking_system.dto.request.TransactionFilterDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.TransactionReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.TransactionResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.exception.AccountNotFoundException;
import ir.digitalbankingsystem.digital_banking_system.exception.BadRequestException;
import ir.digitalbankingsystem.digital_banking_system.exception.InsufficientBalanceException;
import ir.digitalbankingsystem.digital_banking_system.exception.PersonNotFoundException;
import ir.digitalbankingsystem.digital_banking_system.mapper.TransactionMapper;
import ir.digitalbankingsystem.digital_banking_system.repository.AccountRepository;
import ir.digitalbankingsystem.digital_banking_system.repository.PersonRepository;
import ir.digitalbankingsystem.digital_banking_system.repository.TransactionRepository;
import ir.digitalbankingsystem.digital_banking_system.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final PersonRepository personRepository;

    @Override
    @Transactional
    public TransactionResponseDTO deposit(
            TransactionReqDTO.DepositReqDTO request
    ) {

        Account account = getActiveAccount(
                request.accountNumber()
        );

        validateAmount(request.amount());

        depositBalance(
                account,
                request.amount()
        );

        Transaction transaction = createTransaction(
                null,
                account,
                request.amount(),
                Type.DEPOSIT
        );

        transactionRepository.save(transaction);

        return transactionMapper.toResponseDTO(transaction);
    }


    @Override
    @Transactional
    public TransactionResponseDTO withdraw(
            TransactionReqDTO.WithdrawReqDTO request
    ) {

        Account account = getActiveAccount(
                request.accountNumber()
        );

        validateAmount(request.amount());

        withdrawBalance(
                account,
                request.amount()
        );

        Transaction transaction = createTransaction(
                account,
                null,
                request.amount(),
                Type.WITHDRAW
        );

        transactionRepository.save(transaction);

        return transactionMapper.toResponseDTO(transaction);
    }


    @Override
    @Transactional
    public TransactionResponseDTO transfer(
            TransactionReqDTO.TransferReqDTO request
    ) {

        validateDifferentAccounts(
                request.fromAccountNumber(),
                request.toAccountNumber()
        );

        validateAmount(request.amount());

        Account fromAccount;
        Account toAccount;

        if (request.fromAccountNumber().compareTo(request.toAccountNumber()) < 0) {
            fromAccount = getActiveAccount(request.fromAccountNumber());
            toAccount = getActiveAccount(request.toAccountNumber());
        } else {
            toAccount = getActiveAccount(request.toAccountNumber());
            fromAccount = getActiveAccount(request.fromAccountNumber());
        }

        validateSourceAccountOwnership(fromAccount);

        withdrawBalance(
                fromAccount,
                request.amount()
        );

        depositBalance(
                toAccount,
                request.amount()
        );

        Transaction transaction = createTransaction(
                fromAccount,
                toAccount,
                request.amount(),
                Type.TRANSFER
        );

        transactionRepository.save(transaction);

        return transactionMapper.toResponseDTO(transaction);
    }

    @Override
    public Page<TransactionResponseDTO> getMyTransactions(
            TransactionFilterDTO filter,
            Pageable pageable
    ) {

        UUID currentUserCode =
                SecurityUtils.getCurrentUserCode();

        Specification<Transaction> specification =
                buildSpecification(
                        filter,
                        currentUserCode
                );

        Page<Transaction> transactions =
                transactionRepository.findAll(
                        specification,
                        pageable
                );

        return transactions.map(
                transactionMapper::toResponseDTO
        );
    }

    @Override
    public Page<TransactionResponseDTO> getPersonTransactions(
            UUID personCode,
            TransactionFilterDTO filter,
            Pageable pageable
    ) {

        if (personCode == null) {

            throw new BadRequestException(
                    "Person code cannot be null"
            );
        }

        if (!personRepository.existsByUserCode(personCode)) {

            throw new PersonNotFoundException(
                    "Person not found: " + personCode
            );
        }

        Specification<Transaction> specification =
                buildSpecification(
                        filter,
                        personCode
                );

        Page<Transaction> transactions =
                transactionRepository.findAll(
                        specification,
                        pageable
                );

        return transactions.map(
                transactionMapper::toResponseDTO
        );
    }




    private void validateSourceAccountOwnership(Account account) {
        UUID currentUserCode = SecurityUtils.getCurrentUserCode();
        if (!account.getPerson().getUserCode().equals(currentUserCode)) {
            throw new BadRequestException("You can only transfer money from your own account");
        }
    }

    private Account getActiveAccount(
            String accountNumber
    ) {

        Account account = accountRepository
                .findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found: "
                                        + accountNumber
                        )
                );

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BadRequestException(
                    "Account is not active"
            );
        }

        return account;
    }

    private void validateAmount(
            BigDecimal amount
    ) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new BadRequestException(
                    "Amount must be greater than zero"
            );
        }
    }

    private void withdrawBalance(
            Account account,
            BigDecimal amount
    ) {

        if (account.getBalance()
                .compareTo(amount) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance"
            );
        }

        account.setBalance(
                account.getBalance()
                        .subtract(amount)
        );
    }

    private void depositBalance(
            Account account,
            BigDecimal amount
    ) {

        account.setBalance(
                account.getBalance()
                        .add(amount)
        );
    }

    private void validateDifferentAccounts(
            String fromAccountNumber,
            String toAccountNumber
    ) {

        if (fromAccountNumber.equals(toAccountNumber)) {

            throw new BadRequestException(
                    "Source and destination accounts cannot be the same"
            );
        }
    }

    private Transaction createTransaction(
            Account fromAccount,
            Account toAccount,
            BigDecimal amount,
            Type type
    ) {

        Transaction transaction = new Transaction();

        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus(
                TransactionStatus.SUCCESS
        );

        return transaction;
    }

    private Specification<Transaction> buildSpecification(
            TransactionFilterDTO filter,
            UUID personCode
    ) {

        Specification<Transaction> specification =
                (root, query, criteriaBuilder) -> null;


        if (personCode != null) {

            specification =
                    specification.and(
                            TransactionSpecification
                                    .belongsToPerson(personCode)
                    );
        }


        if (filter == null) {

            return specification;
        }


        if (filter.type() != null) {

            specification =
                    specification.and(
                            TransactionSpecification
                                    .hasType(filter.type())
                    );
        }


        if (filter.status() != null) {

            specification =
                    specification.and(
                            TransactionSpecification
                                    .hasStatus(filter.status())
                    );
        }


        if (filter.minAmount() != null) {

            if (filter.minAmount()
                    .compareTo(BigDecimal.ZERO) < 0) {

                throw new BadRequestException(
                        "Minimum amount cannot be negative"
                );
            }

            specification =
                    specification.and(
                            TransactionSpecification
                                    .amountGreaterThanOrEqual(
                                            filter.minAmount()
                                    )
                    );
        }


        if (filter.maxAmount() != null) {

            if (filter.maxAmount()
                    .compareTo(BigDecimal.ZERO) < 0) {

                throw new BadRequestException(
                        "Maximum amount cannot be negative"
                );
            }

            specification =
                    specification.and(
                            TransactionSpecification
                                    .amountLessThanOrEqual(
                                            filter.maxAmount()
                                    )
                    );
        }


        if (filter.minAmount() != null &&
                filter.maxAmount() != null) {

            if (filter.minAmount()
                    .compareTo(filter.maxAmount()) > 0) {

                throw new BadRequestException(
                        "Minimum amount cannot be greater than maximum amount"
                );
            }
        }



        if (filter.fromDate() != null &&
                filter.toDate() != null) {

            if (filter.fromDate()
                    .isAfter(filter.toDate())) {

                throw new BadRequestException(
                        "From date cannot be after to date"
                );
            }
        }


        // از تاریخ مشخص به بعد
        if (filter.fromDate() != null) {

            specification =
                    specification.and(
                            TransactionSpecification
                                    .createdAtAfter(
                                            filter.fromDate()
                                    )
                    );
        }


        // تا تاریخ مشخص
        if (filter.toDate() != null) {

            specification =
                    specification.and(
                            TransactionSpecification
                                    .createdAtBefore(
                                            filter.toDate()
                                    )
                    );
        }


        return specification;
    }


}



