package biz.digissance.graalvmdemo.domain.party;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.party.Party;
import net.liccioni.archetypes.party.Person;

public interface PersonRepository {

    Party save(final Party party);

//    Person update(final Person person);

    List<Person> findAll();

    Optional<Person> findByIdentifier(final String identifier);

    Optional<Party> findByAuthenticationUserName(final String username);

//    Person removeAddress(final String id, final Predicate<AddressProperties> addressMatcher);
}
