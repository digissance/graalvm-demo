package biz.digissance.graalvmdemo.jpa.party.address;

import biz.digissance.graalvmdemo.jpa.party.PartyPK;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAddressRepository extends JpaRepository<JpaAddress, PartyPK> {

    Optional<JpaAddress> findByAddress(String address);

    @Query("from JpaEmailAddress where emailAddress=?1")
    Optional<JpaEmailAddress> findByEmailAddress(String address);

    @Query("from JpaGeographicAddress where address=?1")
    Optional<JpaGeographicAddress> findByGeographicAddress(String address);
}
