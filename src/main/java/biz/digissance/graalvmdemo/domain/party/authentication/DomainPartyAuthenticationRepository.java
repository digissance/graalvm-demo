package biz.digissance.graalvmdemo.domain.party.authentication;

import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DomainPartyAuthenticationRepository implements PartyAuthenticationRepository {

    private final JpaPartyAuthenticationRepository repository;
    private final PartyMapper mapper;

    public DomainPartyAuthenticationRepository(final JpaPartyAuthenticationRepository repository,
                                               final PartyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<EmailPasswordAuthenticationProjection> findPasswordAuthByUsername(final String username) {
        return repository.findPasswordAuthByUsername(username)
                .map(p -> EmailPasswordAuthenticationProjection.builder()
                        .password(p.getPassword())
                        .roles(mapper.toPartyDomain(p.getParty()).getRoles())
                        .build());
    }

    @Override
    public List<EmailPasswordAuthenticationProjection> findAuthByUsername(final String username) {

        return repository.findAuthByUsername(username).stream()
                .map(p -> EmailPasswordAuthenticationProjection.builder()
                        .roles(mapper.toPartyDomain(p.getParty()).getRoles())
                        .build()).collect(Collectors.toList());
    }
}
