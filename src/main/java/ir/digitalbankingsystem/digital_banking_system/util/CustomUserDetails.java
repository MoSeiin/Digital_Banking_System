package ir.digitalbankingsystem.digital_banking_system.util;

import ir.digitalbankingsystem.digital_banking_system.domain.Person;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    @Getter
    private final UUID UserCode;

    public CustomUserDetails(Long id,
                             String username,
                             String password,
                             UUID UserCode,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.UserCode = UserCode;
        this.authorities = authorities;
    }

    public static CustomUserDetails fromPerson(Person person,
                                               Collection<? extends GrantedAuthority> authorities) {
        return new CustomUserDetails(
                person.getId(),
                person.getUserName(),
                person.getPassword(),
                person.getUserCode(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

