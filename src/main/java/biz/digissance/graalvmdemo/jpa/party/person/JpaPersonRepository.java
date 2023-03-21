package biz.digissance.graalvmdemo.jpa.party.person;

import biz.digissance.graalvmdemo.jpa.party.PartyPK;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPersonRepository extends JpaRepository<JpaPerson, PartyPK> {
    @EntityGraph(value = "Party.attributes")
    Optional<JpaPerson> findByIdentifier(String identifier);
}
