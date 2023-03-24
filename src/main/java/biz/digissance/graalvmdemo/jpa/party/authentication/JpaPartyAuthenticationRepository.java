package biz.digissance.graalvmdemo.jpa.party.authentication;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaPartyAuthenticationRepository extends JpaRepository<JpaPartyAuthentication, Long> {

    @Query("from JpaEmailPasswordPartyAuthentication auth " +
            "inner join fetch auth.party party " +
            "inner join fetch party.roles role " +
            "inner join fetch role.type where auth.emailAddress = ?1")
    Optional<JpaPartyAuthentication> findByEmailAddress(String emailAddress);

    @Query("select auth from JpaEmailPasswordPartyAuthentication auth " +
            "inner join fetch auth.party party " +
            "inner join fetch party.roles role " +
            "inner join fetch role.type where auth.emailAddress = ?1")
    Optional<JpaPartyAuthenticationProjection> findByEmailAddress2(String emailAddress);
}
