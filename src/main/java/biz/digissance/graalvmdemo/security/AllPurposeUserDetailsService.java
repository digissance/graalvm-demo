package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.party.authentication.PartyAuthenticationRepository;
import net.liccioni.archetypes.relationship.PartyRole;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AllPurposeUserDetailsService implements UserDetailsService {

    private final PartyAuthenticationRepository repository;
    private final String key;

    public AllPurposeUserDetailsService(final PartyAuthenticationRepository repository, final String key) {
        this.repository = repository;
        this.key = key;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var entry = repository.findAuthByUsername(username).stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.withUsername(username)
                .password(key)
                .authorities(entry.getRoles().stream()
                        .map(PartyRole::getName)
                        .map("ROLE_"::concat)
                        .toArray(String[]::new))
                .build();
    }
}