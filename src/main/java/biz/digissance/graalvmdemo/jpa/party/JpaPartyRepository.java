package biz.digissance.graalvmdemo.jpa.party;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaPartyRepository extends JpaRepository<JpaParty, PartyPK> {

//    @Query("select auth.party from JpaEmailPasswordPartyAuthentication auth " +
//            "inner join fetch auth.party party " +
//            "inner join fetch party.roles role " +
//            "inner join fetch role.type where auth.emailAddress = ?1")
//    Optional<JpaParty> findByEmailAddress(String emailAddress);
}
