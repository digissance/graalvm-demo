package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.uniqueid.UniqueIdentifier;

public class DomainPersonRepository implements PersonRepository {
    private final JpaPersonRepository repository;
    private final PartyMapper mapper;

    public DomainPersonRepository(final JpaPersonRepository jpaPersonRepository,
                                  final PartyMapper mapper) {
        this.repository = jpaPersonRepository;
        this.mapper = mapper;
    }

    @Override
    public Person save(final Person person) {

        final var jpaPerson = Optional.ofNullable(person.getPartyIdentifier())
                .map(UniqueIdentifier::getId)
                .flatMap(repository::findByIdentifier)
                .map(p -> mapper.toPersonJpa(person, p))
                .orElseGet(() -> mapper.toPersonJpa(person));

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

        final var jpaPerson = repository.findByIdentifier(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Person with Id %s not found", id)));

        jpaPerson.getAddressProperties()
                .removeIf(p -> addressMatcher.test(mapper.toAddressProperty(p)));

        return mapper.toPersonDomain(repository.save(jpaPerson));
    }

    @Override
    public Person modifyAddress(final String id,
                                final Predicate<AddressProperties> addressMatcher,
                                final AddressProperties addressProperties) {

        final var jpaPerson = repository.findByIdentifier(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Person with Id %s not found", id)));

        jpaPerson.getAddressProperties()
                .removeIf(p -> addressMatcher.test(mapper.toAddressProperty(p)));
        final var jpaAddressProperty = mapper.toAddressPropertyJpa(addressProperties);
        jpaAddressProperty.setParty(jpaPerson);
        jpaPerson.getAddressProperties().add(jpaAddressProperty);

        return mapper.toPersonDomain(repository.save(jpaPerson));
    }

    @Override
    public Person addAddress(final String id, final AddressProperties addressProperties) {

        final var jpaPerson = repository.findByIdentifier(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Person with Id %s not found", id)));

        final var jpaAddressProperty = mapper.toAddressPropertyJpa(addressProperties);
        jpaAddressProperty.setParty(jpaPerson);
        jpaPerson.getAddressProperties().add(jpaAddressProperty);

        return mapper.toPersonDomain(repository.save(jpaPerson));
    }
}
