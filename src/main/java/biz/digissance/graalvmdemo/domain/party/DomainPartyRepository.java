package biz.digissance.graalvmdemo.domain.party;

import biz.digissance.graalvmdemo.jpa.party.JpaPartyRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import java.util.Optional;
import net.liccioni.archetypes.party.PartyAuthentication;

public class DomainPartyRepository implements PartyRepository {

    private final JpaPartyRepository repository;
    private final JpaPartyAuthenticationRepository authRepository;
    private final PartyMapper mapper;

    public DomainPartyRepository(final JpaPartyRepository repository,
                                 final JpaPartyAuthenticationRepository authRepository,
                                 final PartyMapper mapper) {
        this.repository = repository;
        this.authRepository = authRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<PartyAuthentication> findByEmailAddress(final String emailAddress) {
        return authRepository.findByEmailAddress(emailAddress).map(mapper::toAuthDomain);
    }
}
