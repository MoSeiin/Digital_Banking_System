package ir.digitalbankingsystem.digital_banking_system.repository;

import ir.digitalbankingsystem.digital_banking_system.domain.Person;
import ir.digitalbankingsystem.digital_banking_system.domain.Role;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {

    Optional<Person> findByUserNameOrEmail(String username, String email);

   Optional<Person>findPersonByUserCode(UUID userCode);


    boolean existsByRole(Role role);

    Optional<Person> findByUserName(String username);

    boolean existsByUserCode(UUID personCode);

}
