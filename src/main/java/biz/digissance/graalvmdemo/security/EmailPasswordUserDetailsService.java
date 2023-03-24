package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.PartyRepository;
import biz.digissance.graalvmdemo.jpa.party.JpaPartyRepository;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import net.liccioni.archetypes.relationship.PartyRole;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmailPasswordUserDetailsService implements UserDetailsService {

    private final PartyRepository repository;
    private final JpaPartyRepository jpaRepository;
    private final JpaPartyAuthenticationRepository authRepository;

    public EmailPasswordUserDetailsService(
            final PartyRepository repository,
            final JpaPartyRepository jpaRepository,
            final JpaPartyAuthenticationRepository authRepository) {
        this.repository = repository;
        this.jpaRepository = jpaRepository;
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var entry = authRepository.findByEmailAddress2(username)
//                .map(JpaEmailPasswordPartyAuthentication.class::cast)
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
