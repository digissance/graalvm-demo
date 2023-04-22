package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.party.PartyRepository;
import java.util.UUID;
import net.liccioni.archetypes.relationship.PartyRole;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AllPurposeUserDetailsService implements UserDetailsService {

    private final PartyRepository repository;

    public AllPurposeUserDetailsService(final PartyRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var entry = repository.findByIdentifier(username).stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.withUsername(username)
                .password(UUID.randomUUID().toString())
                .authorities(entry.getRoles().stream()
                        .map(PartyRole::getName)
                        .map("ROLE_"::concat)
                        .toArray(String[]::new))
                .build();
    }
}
