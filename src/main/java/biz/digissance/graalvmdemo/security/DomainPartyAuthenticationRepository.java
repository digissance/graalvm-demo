package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.jpa.party.AuthenticationMapper;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import java.util.Optional;
import net.liccioni.archetypes.party.PartyAuthentication;

public class DomainPartyAuthenticationRepository implements PartyAuthenticationRepository {

    private final JpaPartyAuthenticationRepository repository;
    private final AuthenticationMapper mapper;

    public DomainPartyAuthenticationRepository(final JpaPartyAuthenticationRepository repository,
                                               final AuthenticationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<PartyAuthentication> findByEmailAddress(final String emailAddress) {
        return repository.findByEmailAddress(emailAddress).map(mapper::toDomain);
    }
}
