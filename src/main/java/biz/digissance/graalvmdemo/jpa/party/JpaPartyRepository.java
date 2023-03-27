package biz.digissance.graalvmdemo.jpa.party;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPartyRepository extends JpaRepository<JpaParty, PartyPK> {

//    @Query("select auth.party from JpaEmailPasswordPartyAuthentication auth " +
//            "inner join fetch auth.party party " +
//            "inner join fetch party.roles role " +
//            "inner join fetch role.type where auth.emailAddress = ?1")
//    Optional<JpaParty> findByEmailAddress(String emailAddress);
}
