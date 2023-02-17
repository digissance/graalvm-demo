package biz.digissance.graalvmdemo.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, PartyPK> {
    Optional<OrganizationEntity> findByIdentifier(String identifier);
}
