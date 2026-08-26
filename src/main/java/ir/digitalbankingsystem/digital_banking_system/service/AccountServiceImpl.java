package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.config.SecurityUtils;
import ir.digitalbankingsystem.digital_banking_system.domain.*;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AccountResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.exception.BadRequestException;
import ir.digitalbankingsystem.digital_banking_system.exception.PersonNotFoundException;
import ir.digitalbankingsystem.digital_banking_system.mapper.AccountMapper;
import ir.digitalbankingsystem.digital_banking_system.repository.AccountRepository;
import ir.digitalbankingsystem.digital_banking_system.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;
    private final AccountMapper accountMapper;
    private final CardService cardService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, PersonRepository personRepository, AccountMapper accountMapper, CardService cardService) {
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
        this.accountMapper = accountMapper;
        this.cardService = cardService;
    }


    @Override
    public AccountResponseDTO createAccount(UUID personCode) {

        Person person = findPersonByPersonCode(personCode);

        if (person.getStatus() != Status.APPROVED) {
            throw new BadRequestException(
                    "Person must be approved to create an account"
            );
        }
        if (person.getRole() ==Role.ADMIN) throw new BadRequestException("you can't create a account for admin");

        Account account = new Account();

        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(Currency.IRR);
        account.setStatus(AccountStatus.ACTIVE);
        account.setPerson(person);

        Account savedAccount = accountRepository.save(account);
        cardService.issueCard(savedAccount.getAccountNumber());
        Account accountWithCard = accountRepository.findByAccountNumber(savedAccount.getAccountNumber())
                .orElseThrow(() -> new BadRequestException("Account was not created"));

        return accountMapper.toAccountResponseDTO(accountWithCard);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponseDTO> getMyAccounts(Pageable pageable) {
        UUID currentUserCode = SecurityUtils.getCurrentUserCode();
        return accountRepository.findAllByPerson_UserCode(currentUserCode, pageable)
                .map(accountMapper::toAccountResponseDTO);
    }

    @Override
    public AccountResponseDTO closeAccount(
            String accountNumber
    ) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                        .orElseThrow(() -> new BadRequestException("Account not found"));

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new BadRequestException(
                    "Account is already closed"
            );
        }

        account.setStatus(AccountStatus.CLOSED);

        return accountMapper
                .toAccountResponseDTO(account);
    }

    private Person findPersonByPersonCode(UUID personCode) {
        return personRepository.findPersonByUserCode(personCode).orElseThrow(() -> new PersonNotFoundException("Person not found"));
    }

    private String generateAccountNumber() {
        return "IR" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 14)
                .toUpperCase();
    }
}
