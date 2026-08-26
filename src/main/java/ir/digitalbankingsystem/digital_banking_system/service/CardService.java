package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.dto.response.CardResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CardService {
    CardResponseDTO issueCard(String accountNumber);
    Page<CardResponseDTO> getMyCards(Pageable pageable);
    CardResponseDTO blockCard(String cardNumber);
    CardResponseDTO activateCard(String cardNumber);
}
