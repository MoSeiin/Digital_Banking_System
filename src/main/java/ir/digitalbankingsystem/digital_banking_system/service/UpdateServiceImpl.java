package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.domain.Person;
import ir.digitalbankingsystem.digital_banking_system.domain.Role;
import ir.digitalbankingsystem.digital_banking_system.config.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import ir.digitalbankingsystem.digital_banking_system.dto.request.ChangePasswordReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.UpdateUserReqDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AuthResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.exception.BadRequestException;
import ir.digitalbankingsystem.digital_banking_system.mapper.AdminMapper;
import ir.digitalbankingsystem.digital_banking_system.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UpdateServiceImpl implements UpdateService {
    private final PersonRepository personRepository;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UpdateServiceImpl(PersonRepository personRepository, AdminMapper adminMapper, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AuthResponseDTO updateUser(UUID userCode, UpdateUserReqDTO updateUserReqDTO) {
        ensureCurrentUser(userCode);
        Person person = personRepository.findPersonByUserCode(userCode).orElseThrow(() -> new BadRequestException("UserCode not found"));

        if (person.getRole() == Role.ADMIN) {
            throw new BadRequestException("Cannot change role of admin");
        }
        adminMapper.updatePerson(updateUserReqDTO, person);

        return adminMapper.toAuthResponseDTO(person);

    }

    @Override
    public void changePassword(UUID userCode, ChangePasswordReqDTO changePasswordReqDTO) {
        ensureCurrentUser(userCode);
        Person person = personRepository.findPersonByUserCode(userCode).orElseThrow(() -> new BadRequestException("UserCode not found"));

        if (!passwordEncoder.matches(changePasswordReqDTO.oldPassword(), person.getPassword())) {
            throw new BadRequestException("Old Password id incorrect");
        }
        if (!changePasswordReqDTO.newPassword().equals(changePasswordReqDTO.confirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        if (passwordEncoder.matches(changePasswordReqDTO.newPassword(), person.getPassword())) {
            throw new BadRequestException("New password cannot be same as old password");
        }

        person.setPassword(passwordEncoder.encode(changePasswordReqDTO.newPassword()));


    }
    private void ensureCurrentUser(UUID userCode) {
        if (!SecurityUtils.getCurrentUserCode().equals(userCode)) {
            throw new AccessDeniedException("You can only modify your own user data");
        }
    }

}
