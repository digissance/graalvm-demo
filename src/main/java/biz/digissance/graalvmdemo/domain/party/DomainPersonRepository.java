package biz.digissance.graalvmdemo.domain.party;

import biz.digissance.graalvmdemo.jpa.party.JpaPartyRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPerson;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.liccioni.archetypes.party.Party;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.uniqueid.UniqueIdentifier;

public class DomainPersonRepository implements PersonRepository {
    private final JpaPersonRepository repository;
    private final JpaPartyRepository partyRepository;
    private final PartyMapper mapper;

    public DomainPersonRepository(final JpaPersonRepository jpaPersonRepository,
                                  final JpaPartyRepository partyRepository,
                                  final PartyMapper mapper) {
        this.repository = jpaPersonRepository;
        this.partyRepository = partyRepository;
        this.mapper = mapper;
    }

    @Override
    public Party save(final Party person) {

        final var jpaPerson = Optional.ofNullable(person.getIdentifier())
                .map(UniqueIdentifier::getId)
                .flatMap(partyRepository::findByIdentifier)
                .map(p -> mapper.toPartyJpaForUpdate(person, p, p)).orElseGet(() -> mapper.toPartyJpa(person));
        final var savedJpa = partyRepository.save(jpaPerson);
        return mapper.toPartyDomain(savedJpa);
    }

    @Override
    public List<Person> findAll() {
        return repository.findAll().stream()
                .map((JpaPerson personDto) -> {
                    return (Person) mapper.toPartyDomain(personDto);
                }).collect(Collectors.toList());
    }

    @Override
    public Optional<Person> findByIdentifier(final String identifier) {
        return repository.findByIdentifier(identifier).map((JpaPerson personDto) -> {
            return (Person) mapper.toPartyDomain(personDto);
        });
    }

    @Override
    public Optional<Party> findByAuthenticationUserName(final String username) {
        return partyRepository.findPartyByUsername(username).map(mapper::toPartyDomain);
    }
}
