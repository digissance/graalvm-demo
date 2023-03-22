package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPerson;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.party.Person;

public class DomainPersonRepository implements PersonRepository {
    private final JpaPersonRepository repository;
    private final PartyMapper mapper;

    public DomainPersonRepository(final JpaPersonRepository jpaPersonRepository,
                                  final PartyMapper mapper) {
        this.repository = jpaPersonRepository;
        this.mapper = mapper;
    }

    @Override
    public Person create(final Person person) {

        final var jpaPerson = mapper.toPersonJpa(person);
        final var savedJpa = repository.save(jpaPerson);
        return mapper.toPersonDomain(savedJpa);
    }

    @Override
    public Person update(final Person person) {
        final var jpaPerson = repository.findByIdentifier(person.getPartyIdentifier().getId()).orElseThrow();
        mapper.toPersonJpa(person, jpaPerson);
        return mapper.toPersonDomain(repository.save(jpaPerson));
    }

    @Override
    public List<Person> findAll() {
        return repository.findAll().stream()
                .map(mapper::toPersonDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Person> findByIdentifier(final String identifier) {
        return repository.findByIdentifier(identifier).map(mapper::toPersonDomain);
    }

    @Override
    public Person removeAddress(final String id, final Predicate<AddressProperties> addressMatcher) {

        final var jpaPerson = findByIdentifierOrThrowException(id);
        jpaPerson.getAddressProperties().removeIf(p -> addressMatcher.test(mapper.toAddressProperty(p)));
        return mapper.toPersonDomain(repository.save(jpaPerson));
    }

    private JpaPerson findByIdentifierOrThrowException(final String id) {
        return repository.findByIdentifier(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Person with Id %s not found", id)));
    }
}
