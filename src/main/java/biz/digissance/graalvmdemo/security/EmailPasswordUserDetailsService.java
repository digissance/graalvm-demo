package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.party.authentication.PartyAuthenticationRepository;
import net.liccioni.archetypes.relationship.PartyRole;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmailPasswordUserDetailsService implements UserDetailsService {

    private final PartyAuthenticationRepository repository;

    public EmailPasswordUserDetailsService(final PartyAuthenticationRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var entry = repository.findPasswordAuthByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.withUsername(entry.getPartyIdentifier())
                .password(entry.getPassword())
                .authorities(entry.getRoles().stream()
                        .map(PartyRole::getName)
                        .map("ROLE_"::concat)
                        .toArray(String[]::new))
                .build();
    }
}
