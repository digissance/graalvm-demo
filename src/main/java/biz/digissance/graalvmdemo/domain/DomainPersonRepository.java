package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.jpa.JpaPersonRepository;
import biz.digissance.graalvmdemo.jpa.PersonMapper;
import java.util.List;
import java.util.stream.Collectors;
import net.liccioni.archetypes.party.Person;

public class DomainPersonRepository implements PersonRepository {
    private final JpaPersonRepository repository;
    private final PersonMapper mapper;

    public DomainPersonRepository(final JpaPersonRepository jpaPersonRepository,
                                  final PersonMapper mapper) {
        this.repository = jpaPersonRepository;
        this.mapper = mapper;
    }

    @Override
    public Person save(final Person person) {
        return mapper.toPerson(repository.save(mapper.toPersonEntity(person)));
    }

    @Override
    public List<Person> findAll() {
        return repository.findAll().stream()
                .map(mapper::toPerson).collect(Collectors.toList());
    }
}
