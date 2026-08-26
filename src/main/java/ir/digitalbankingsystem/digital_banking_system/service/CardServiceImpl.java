package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.config.SecurityUtils;
import ir.digitalbankingsystem.digital_banking_system.domain.*;
import ir.digitalbankingsystem.digital_banking_system.dto.response.CardResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.exception.AccountNotFoundException;
import ir.digitalbankingsystem.digital_banking_system.exception.BadRequestException;
import ir.digitalbankingsystem.digital_banking_system.mapper.CardMapper;
import ir.digitalbankingsystem.digital_banking_system.repository.AccountRepository;
import ir.digitalbankingsystem.digital_banking_system.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CardMapper cardMapper;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public CardResponseDTO issueCard(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BadRequestException("Only active accounts can receive a card");
        }

        Card card = new Card();
        card.setCardNumber(generateUniqueCardNumber());
        card.setExpiryDate(LocalDate.now().plusYears(5));
        card.setStatus(CardStatus.ACTIVE);
        card.setAccount(account);

        return cardMapper.toResponseDTO(cardRepository.save(card));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponseDTO> getMyCards(Pageable pageable) {
        return cardRepository.findAllByAccount_Person_UserCode(
                SecurityUtils.getCurrentUserCode(), pageable
        ).map(cardMapper::toResponseDTO);
    }

    @Override
    public CardResponseDTO blockCard(String cardNumber) {
        Card card = getCardForUpdate(cardNumber);
        if (card.getStatus() == CardStatus.EXPIRED) {
            throw new BadRequestException("Expired card cannot be blocked");
        }
        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new BadRequestException("Card is already blocked");
        }
        card.setStatus(CardStatus.BLOCKED);
        return cardMapper.toResponseDTO(card);
    }

    @Override
    public CardResponseDTO activateCard(String cardNumber) {
        Card card = getCardForUpdate(cardNumber);
        if (card.getExpiryDate().isBefore(LocalDate.now())) {
            card.setStatus(CardStatus.EXPIRED);
            throw new BadRequestException("Card is expired");
        }
        if (card.getStatus() == CardStatus.ACTIVE) {
            throw new BadRequestException("Card is already active");
        }
        card.setStatus(CardStatus.ACTIVE);
        return cardMapper.toResponseDTO(card);
    }

    private Card getCardForUpdate(String cardNumber) {
        return cardRepository.findByCardNumberForUpdate(cardNumber)
                .orElseThrow(() -> new BadRequestException("Card not found"));
    }

    private String generateUniqueCardNumber() {
        while (true) {
            String base = "6037" + String.format("%011d", Math.abs(secureRandom.nextLong()) % 100_000_000_000L);
            String number = base + calculateLuhnCheckDigit(base);
            if (!cardRepository.existsByCardNumber(number)) {
                return number;
            }
        }
    }

    private int calculateLuhnCheckDigit(String base) {
        int sum = 0;
        boolean doubleDigit = true;
        for (int i = base.length() - 1; i >= 0; i--) {
            int digit = base.charAt(i) - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        return (10 - (sum % 10)) % 10;
    }
}
