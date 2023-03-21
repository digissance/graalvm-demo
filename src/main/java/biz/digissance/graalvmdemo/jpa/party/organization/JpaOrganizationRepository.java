package biz.digissance.graalvmdemo.jpa.party.organization;

import biz.digissance.graalvmdemo.jpa.party.PartyPK;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrganizationRepository extends JpaRepository<JpaOrganization, PartyPK> {
    Optional<JpaOrganization> findByIdentifier(String identifier);
}
