package biz.digissance.graalvmdemo.domain.party.authentication;

import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaEmailPasswordPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import java.util.Optional;

public class DomainEmailPasswordPartyAuthenticationRepository implements EmailPasswordPartyAuthenticationRepository {

    private final JpaPartyAuthenticationRepository repository;
    private final PartyMapper mapper;

    public DomainEmailPasswordPartyAuthenticationRepository(final JpaPartyAuthenticationRepository repository,
                                                            final PartyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<EmailPasswordAuthenticationProjection> findByEmailAddress(final String username) {
        return repository.findByEmailAddress(username)
                .map(JpaEmailPasswordPartyAuthentication.class::cast)
                .map(p -> EmailPasswordAuthenticationProjection.builder()
                        .password(p.getPassword())
                        .roles(mapper.toDomain(p.getParty()).getRoles())
                        .build());
    }
}
