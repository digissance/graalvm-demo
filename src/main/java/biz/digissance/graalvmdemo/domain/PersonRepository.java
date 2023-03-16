package biz.digissance.graalvmdemo.domain;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.party.Person;

public interface PersonRepository {
    Person save(Person gus);

    List<Person> findAll();

    Optional<Person> findByIdentifier(String identifier);

    void removeAddress(final String id, final Predicate<AddressProperties> addressMatcher);
}
