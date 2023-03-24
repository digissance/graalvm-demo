package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.party.authentication.EmailPasswordPartyAuthenticationRepository;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import net.liccioni.archetypes.relationship.PartyRole;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmailPasswordUserDetailsService implements UserDetailsService {

    private final JpaPartyAuthenticationRepository authRepository;
    private final EmailPasswordPartyAuthenticationRepository repository;

    public EmailPasswordUserDetailsService(final JpaPartyAuthenticationRepository authRepository,
                                           final EmailPasswordPartyAuthenticationRepository repository) {
        this.authRepository = authRepository;
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var entry = repository.findByEmailAddress(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.withUsername(username)
                .password(entry.getPassword())
                .authorities(entry.getRoles().stream()
                        .map(PartyRole::getName)
                        .map("ROLE_"::concat)
                        .toArray(String[]::new))
                .build();
    }
}
