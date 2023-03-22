package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.jpa.party.authentication.JpaEmailPasswordPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmailPasswordUserDetailsService implements UserDetailsService {

    private final PartyAuthenticationRepository repository;
    private final JpaPartyAuthenticationRepository jpaRepository;

    public EmailPasswordUserDetailsService(
            final PartyAuthenticationRepository repository,
            final JpaPartyAuthenticationRepository jpaRepository) {
        this.repository = repository;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var entry = jpaRepository.findByEmailAddress(username)
                .map(JpaEmailPasswordPartyAuthentication.class::cast)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.withUsername(username)
                .password(entry.getPassword())
                .authorities(entry.getParty().getRoles().stream()
                        .map(p -> p.getType().getName()).toArray(String[]::new))
                .build();
    }
}
