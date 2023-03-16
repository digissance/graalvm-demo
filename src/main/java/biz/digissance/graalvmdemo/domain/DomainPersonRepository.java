package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
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
    public Person save(final Person person) {
        final var jpaPerson = mapper.toPersonJpa(person);
        jpaPerson.getAddressProperties().forEach(p -> p.setParty(jpaPerson));
        final var save = repository.save(jpaPerson);
        return mapper.toPersonDomain(save);
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
    public void removeAddress(final String id, final Predicate<AddressProperties> addressMatcher) {
        repository.findByIdentifier(id)
                .ifPresentOrElse(
                        jpaPerson -> {
                            jpaPerson.getAddressProperties()
                                    .removeIf(p -> addressMatcher.test(mapper.toAddressPropertyJpa(p)));
                            repository.save(jpaPerson);
                        },
                        () -> {
                            throw new IllegalArgumentException(String.format("Person with Id %s not found", id));
                        });
    }
}
