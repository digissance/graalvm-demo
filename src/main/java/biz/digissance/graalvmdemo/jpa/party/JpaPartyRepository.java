package biz.digissance.graalvmdemo.jpa.party;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaPartyRepository extends JpaRepository<JpaParty, PartyPK> {

    @EntityGraph(value = "Party.attributes")
    Optional<JpaParty> findByIdentifier(String identifier);

    @Query("select p from JpaParty p " +
            "inner join fetch p.authentications auth " +
            "inner join fetch p.addressProperties ap " +
            "inner join fetch ap.use apu " +
            "inner join fetch ap.address address " +
            "inner join fetch p.roles role " +
            "inner join fetch role.type where auth.username = ?1")
    Optional<JpaParty> findPartyByUsername(String username);
}
