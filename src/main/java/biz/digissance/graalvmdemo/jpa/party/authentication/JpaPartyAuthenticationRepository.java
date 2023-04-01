package biz.digissance.graalvmdemo.jpa.party.authentication;

import biz.digissance.graalvmdemo.jpa.party.JpaParty;
import java.util.List;
import java.util.Optional;
import net.liccioni.archetypes.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaPartyAuthenticationRepository extends JpaRepository<JpaPartyAuthentication, Long> {

    @Query("from JpaEmailPasswordPartyAuthentication auth " +
            "inner join fetch auth.party party " +
            "inner join fetch party.roles role " +
            "inner join fetch role.type where auth.username = ?1")
    Optional<JpaEmailPasswordPartyAuthentication> findPasswordAuthByUsername(String emailAddress);

    @Query("from JpaOidcPartyAuthentication auth " +
            "inner join fetch auth.party party " +
            "inner join fetch party.roles role " +
            "inner join fetch role.type where auth.username = ?1")
    Optional<JpaOidcPartyAuthentication> findOidcAuthByUsername(String emailAddress);

    @Query("from JpaPartyAuthentication auth " +
            "inner join fetch auth.party party " +
            "inner join fetch party.roles role " +
            "inner join fetch role.type where auth.username = ?1")
    List<JpaPartyAuthentication> findAuthByUsername(String username);
}
