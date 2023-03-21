package biz.digissance.graalvmdemo.jpa.party.address;

import biz.digissance.graalvmdemo.jpa.party.PartyPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAddressRepository extends JpaRepository<JpaAddress, PartyPK> {

}
