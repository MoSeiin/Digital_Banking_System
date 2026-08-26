package ir.digitalbankingsystem.digital_banking_system.service;

import ir.digitalbankingsystem.digital_banking_system.domain.Person;
import ir.digitalbankingsystem.digital_banking_system.domain.Role;
import ir.digitalbankingsystem.digital_banking_system.domain.Status;
import ir.digitalbankingsystem.digital_banking_system.dto.request.LoginRequestDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.request.RegisterRequestDTO;
import ir.digitalbankingsystem.digital_banking_system.dto.response.AuthResponseDTO;
import ir.digitalbankingsystem.digital_banking_system.exception.BadRequestException;
import ir.digitalbankingsystem.digital_banking_system.mapper.AuthMapper;
import ir.digitalbankingsystem.digital_banking_system.repository.PersonRepository;
import ir.digitalbankingsystem.digital_banking_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final JwtUtil jwtUtil;
    @Autowired
    public AuthServiceImpl(PersonRepository personRepository, PasswordEncoder passwordEncoder, AuthMapper authMapper, JwtUtil jwtUtil) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.authMapper = authMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        Person person = authMapper.toPerson(registerRequestDTO);
        person.setUserName(person.getUserName().trim().toLowerCase());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        if (registerRequestDTO.role() == Role.ADMIN) throw new BadRequestException("You cannot register as ADMIN");
        person.setRole(registerRequestDTO.role());
        personRepository.save(person);
        return authMapper.toAuthResponseDTO(person);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request) {

        Person person = personRepository.findByUserNameOrEmail(request.userName() , request.email())
                .orElseThrow(() -> new BadRequestException("user not found"));

        if (!passwordEncoder.matches(request.password(), person.getPassword())) {
            throw new BadRequestException("user not found");
        }

        if (person.getStatus() == Status.BLOCKED) {
            throw new BadRequestException("User is blocked");
        }
        if (person.getStatus() != Status.APPROVED) {
            throw new BadRequestException("User is not approved yet");
        }
        String token = jwtUtil.generateToken(person.getUserName());

        return new AuthResponseDTO(
                token,
                person.getUserCode(),
                person.getUserName(),
                person.getRole(),
                person.getStatus()
        );


    }
}
