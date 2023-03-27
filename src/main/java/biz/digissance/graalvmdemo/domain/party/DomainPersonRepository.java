package biz.digissance.graalvmdemo.domain.party;

import biz.digissance.graalvmdemo.jpa.party.JpaPartyRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPerson;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.liccioni.archetypes.party.Person;

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
    public Person create(final Person person) {

        final var jpaPerson = mapper.toPartyJpa(person);
        final var savedJpa = partyRepository.save(jpaPerson);
        return (Person) mapper.toPartyDomain(savedJpa);
    }

    @Override
    public Person update(final Person person) {
        final var jpaPerson = repository.findByIdentifier(person.getPartyIdentifier().getId()).orElseThrow();
        mapper.toPersonJpaForUpdate(person, jpaPerson, jpaPerson);
        return (Person) mapper.toPartyDomain(partyRepository.save(jpaPerson));
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

    /*@Override
    public Person removeAddress(final String id, final Predicate<AddressProperties> addressMatcher) {

        final var jpaPerson = findByIdentifierOrThrowException(id);
        jpaPerson.getAddressProperties().removeIf(p -> addressMatcher.test(mapper.toAddressProperty(p)));
        return mapper.toPersonDomain(repository.save(jpaPerson));
    }*/

    private JpaPerson findByIdentifierOrThrowException(final String id) {
        return repository.findByIdentifier(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Person with Id %s not found", id)));
    }
}
