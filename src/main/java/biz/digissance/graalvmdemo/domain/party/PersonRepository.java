package biz.digissance.graalvmdemo.domain.party;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.party.Person;

public interface PersonRepository {

    Person create(final Person person);

    Person update(Person person);

    List<Person> findAll();

    Optional<Person> findByIdentifier(final String identifier);

    Person removeAddress(final String id, final Predicate<AddressProperties> addressMatcher);
}
