package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.domain.*;
import ir.digitalbankingsystem.digital_banking_system.dto.request.ChangeUserRoleReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.TransactionFilterDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.*;
import ir.digitalbankingsystem.digital_banking_system.exception.BadRequestException;
import ir.digitalbankingsystem.digital_banking_system.exception.PersonNotFoundException;
import ir.digitalbankingsystem.digital_banking_system.mapper.TransactionMapper;
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
@Transactional
public class AdminServiceImpl implements AdminService {

    private final PersonRepository personRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public void approveUser(UUID userCode) {
        Person person = findPerson(userCode);
        if (person.getRole() == Role.ADMIN) {
            throw new BadRequestException("Admin is already privileged");
        }
        if (person.getStatus() == Status.APPROVED) {
            throw new BadRequestException("User is already approved");
        }
        if (person.getStatus() == Status.BLOCKED) {
            throw new BadRequestException("Blocked user must be unblocked explicitly");
        }
        person.setStatus(Status.APPROVED);
    }

    @Override
    public void rejectUser(UUID userCode) {
        Person person = findPerson(userCode);
        if (person.getStatus() == Status.REJECTED) {
            throw new BadRequestException("User is already rejected");
        }
        person.setStatus(Status.REJECTED);
    }

    @Override
    public void blockUser(UUID userCode) {
        Person person = findPerson(userCode);
        if (person.getRole() == Role.ADMIN) {
            throw new BadRequestException("Cannot block an admin");
        }
        if (person.getStatus() == Status.BLOCKED) {
            throw new BadRequestException("User is already blocked");
        }
        person.setStatus(Status.BLOCKED);
    }

    @Override
    public void unblockUser(UUID userCode) {
        Person person = findPerson(userCode);
        if (person.getStatus() != Status.BLOCKED) {
            throw new BadRequestException("User is not blocked");
        }
        person.setStatus(Status.APPROVED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminUserResponseDTO> getUsers(Pageable pageable) {
        return personRepository.findAll(pageable).map(this::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponseDTO getUser(UUID userCode) {
        return toUserResponse(findPerson(userCode));
    }

    @Override
    public AdminUserResponseDTO changeRole(UUID userCode, ChangeUserRoleReqDTO request) {
        Person person = findPerson(userCode);
        if (person.getRole() == Role.ADMIN) {
            throw new BadRequestException("Cannot change the role of an admin");
        }
        if (request.role() == Role.ADMIN) {
            throw new BadRequestException("Promoting a user to ADMIN is disabled");
        }
        person.setRole(request.role());
        if (person.getStatus() == Status.REJECTED && request.role() != Role.ADMIN) {
            person.setStatus(Status.PENDING);
        }
        return toUserResponse(person);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> getTransactions(TransactionFilterDTO filter, Pageable pageable) {
        Specification<Transaction> specification = buildTransactionSpecification(filter);
        return transactionRepository.findAll(specification, pageable).map(transactionMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionSummaryResponseDTO getTransactionSummary() {
        return new TransactionSummaryResponseDTO(
                transactionRepository.count(),
                transactionRepository.countByStatus(TransactionStatus.SUCCESS),
                transactionRepository.countByStatus(TransactionStatus.FAILED),
                safeSum(Type.DEPOSIT),
                safeSum(Type.WITHDRAW),
                safeSum(Type.TRANSFER)
        );
    }

    private BigDecimal safeSum(Type type) {
        return transactionRepository.sumAmountByTypeAndStatus(type, TransactionStatus.SUCCESS);
    }

    private Specification<Transaction> buildTransactionSpecification(TransactionFilterDTO filter) {
        Specification<Transaction> spec = (root, query, cb) -> null;
        if (filter == null) return spec;
        if (filter.type() != null) spec = spec.and(TransactionSpecification.hasType(filter.type()));
        if (filter.status() != null) spec = spec.and(TransactionSpecification.hasStatus(filter.status()));
        if (filter.minAmount() != null) {
            if (filter.minAmount().compareTo(BigDecimal.ZERO) < 0) throw new BadRequestException("Minimum amount cannot be negative");
            spec = spec.and(TransactionSpecification.amountGreaterThanOrEqual(filter.minAmount()));
        }
        if (filter.maxAmount() != null) {
            if (filter.maxAmount().compareTo(BigDecimal.ZERO) < 0) throw new BadRequestException("Maximum amount cannot be negative");
            spec = spec.and(TransactionSpecification.amountLessThanOrEqual(filter.maxAmount()));
        }
        if (filter.minAmount() != null && filter.maxAmount() != null && filter.minAmount().compareTo(filter.maxAmount()) > 0) {
            throw new BadRequestException("Minimum amount cannot be greater than maximum amount");
        }
        if (filter.fromDate() != null) spec = spec.and(TransactionSpecification.createdAtAfter(filter.fromDate()));
        if (filter.toDate() != null) spec = spec.and(TransactionSpecification.createdAtBefore(filter.toDate()));
        if (filter.fromDate() != null && filter.toDate() != null && filter.fromDate().isAfter(filter.toDate())) {
            throw new BadRequestException("From date cannot be after to date");
        }
        return spec;
    }

    private Person findPerson(UUID userCode) {
        if (userCode == null) throw new BadRequestException("User code cannot be null");
        return personRepository.findPersonByUserCode(userCode)
                .orElseThrow(() -> new PersonNotFoundException("Person not found: " + userCode));
    }

    private AdminUserResponseDTO toUserResponse(Person person) {
        return new AdminUserResponseDTO(
                person.getUserCode(), person.getFirstName(), person.getLastName(),
                person.getUserName(), person.getEmail(), person.getPhoneNumber(),
                person.getAge(), person.getGender(), person.getRole(), person.getStatus(),
                person.getCreateAt()
        );
    }
}
